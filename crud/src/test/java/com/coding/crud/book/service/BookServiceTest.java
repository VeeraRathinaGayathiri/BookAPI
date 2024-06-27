package com.coding.crud.book.service;

import com.coding.crud.book.model.Book;
import com.coding.crud.book.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    BookService bookService;

     @Mock
     BookRepository bookRepository;

    @Test
    void ShouldCallsaveBook() {
        Book book = new Book("Eat that Frog","Smith", 200, 5);

        when(bookRepository.save(any())).thenReturn(book);
        ResponseEntity<Book> createdBook = bookService.createBook(book);
        assertEquals( HttpStatus.CREATED, createdBook.getStatusCode());
    }

    @Test
    void getAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Ansa", "ana", 190, 14));
        books.add(new Book("Bella", "bella", 500, 12));
        when(bookRepository.findAll()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookService.getBooks(null);
        assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode() ),
                () -> assertEquals(2, response.getBody().size())) ;
    }

    @Test
    void shouldGiveNoContentStatus() {
        List<Book> books = new ArrayList<>();
        /*books.add(new Book("Ansa", "ansa", 190, 14));
        books.add(new Book("Ana", "Ana", 500, 12));*/
        when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(Optional.empty());

        ResponseEntity<List<Book>> response = bookService.getBooks("Ikigai");
        assertAll(() -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertEquals(null , response.getBody())) ;
    }

    @Test
    void shouldReturnBookListOfMAtchingTitles() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Ansa", "ansa", 190, 14));
        books.add(new Book("Ana", "Ana", 500, 12));
        when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(Optional.of(books));

        ResponseEntity<List<Book>> response = bookService.getBooks("an");
        assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(2, response.getBody().size())) ;
    }

    @Test
    void updateBook() {
        Book book = new Book("5am club", "Robert", 250, 23);

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        ResponseEntity<Book> result = bookService.updateBook(52L,book);
        assertEquals(HttpStatus.OK, result.getStatusCode());


    }

    @Test
    void deleteBook() {
        Book book = new Book("5am club", "Robert", 250, 23);

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));

        doNothing().when(bookRepository).deleteById(any());

        ResponseEntity result = bookService.deleteBook(5L);

        assertEquals(HttpStatus.OK, result.getStatusCode());

    }
}