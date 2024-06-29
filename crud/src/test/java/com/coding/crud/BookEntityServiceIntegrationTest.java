package com.coding.crud;


import com.coding.crud.book.dto.BookRequest;
import com.coding.crud.book.model.BookEntity;
import com.coding.crud.book.repository.BookRepository;
import com.coding.crud.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookEntityServiceIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    private static HttpHeaders headers;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void init(){
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

    }
    private String createURL(){
        return "http://localhost:" + port + "/books";
    }

    @Test
    public void TestCreateBook() throws JsonProcessingException {
        BookRequest book = new BookRequest("Happy days", "Annie", 100, 10 );
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(book), headers);

        ResponseEntity<BookEntity> response = testRestTemplate.exchange(createURL(), HttpMethod.POST, entity, BookEntity.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        BookEntity bookRes = Objects.requireNonNull(response.getBody());
        assertAll(() -> assertEquals(book.getTitle(), bookRes.getTitle()),
                () -> assertEquals(book.getPrice(), bookRes.getPrice()));

    }

    @Test
    public void TestFetchBook(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<BookEntity>> response = testRestTemplate.exchange(createURL(),
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<BookEntity>>(){});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void TestFetchBookmatchTitle(){

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<BookEntity>> response = testRestTemplate.exchange(createURL() + "/Hap",
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<BookEntity>>(){});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<BookEntity> book = response.getBody();
        List<String> titles = book.stream().map(x -> x.getTitle()).collect(Collectors.toList());
            assertTrue(titles.stream().allMatch(x -> x.contains("Hap")));
    }

    @Test
    public void TestDeleteBook(){
        ResponseEntity<String> response = testRestTemplate.exchange(createURL()+"/1", HttpMethod.DELETE, null, String.class);
       assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void TestUpdateBook() throws JsonProcessingException {
        BookRequest updatedBook = new BookRequest("Happy days", "Harry", 100, 30);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(updatedBook), headers);

        ResponseEntity<BookEntity> response = testRestTemplate.exchange(createURL()+ "/2",
                                                                    HttpMethod.PUT,
                                                                    entity,
                                                                    BookEntity.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBook.getAuthor(), response.getBody().getAuthor());

    }
}

