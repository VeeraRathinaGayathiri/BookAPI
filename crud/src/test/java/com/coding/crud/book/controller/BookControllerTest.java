package com.coding.crud.book.controller;

import com.coding.crud.book.model.Book;
import com.coding.crud.book.repository.BookRepository;
import com.coding.crud.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testCreateBook() throws Exception {
        Book book = new Book();
        when(bookService.createBook(any(Book.class))).thenReturn(new ResponseEntity<>(book, HttpStatus.CREATED));

        mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content("{\"title\": \"Ikigai\"" +
                                "\"author\": \"John\"" +
                                "\"price\": \100" +
                                "\"quantity\": \10}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        when(bookService.getBooks(null)).thenReturn(new ResponseEntity<>(books, HttpStatus.OK));

        mockMvc.perform(get("/books/"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBookByTitle() throws Exception {
        List<Book> books = new ArrayList<>();
        when(bookService.getBooks(anyString())).thenReturn(new ResponseEntity<>(books, HttpStatus.OK));

        mockMvc.perform(get("/books/Test%20Book"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = new Book();
        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(new ResponseEntity<>(book, HttpStatus.OK));

        mockMvc.perform(put("/books/1")
                        .contentType("application/json")
                        .content("{\"title\": \"Updated Book\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteBook() throws Exception {
        when(bookService.deleteBook(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }
}
