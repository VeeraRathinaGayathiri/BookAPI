package com.coding.crud.book.controller;


import com.coding.crud.book.dto.BookRequest;
import com.coding.crud.book.dto.BookResponse;
import com.coding.crud.book.repository.BookRepository;
import com.coding.crud.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookEntityControllerTest {
    @MockBean
    BookRepository bookRepository;

    @MockBean
    BookService bookService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCallCreateBookService() throws Exception{
        BookRequest book = new BookRequest("Ikikai", "John", 100, 10);

        when(bookService.createBook(any(BookRequest.class))).thenReturn(new ResponseEntity<>(new BookResponse(), HttpStatus.CREATED));
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldCallListAllBooks() throws Exception {
        List<BookResponse> books = new ArrayList<>();

        when(bookService.getBooks(any())).thenReturn(new ResponseEntity<>(books, HttpStatus.OK));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCallListBooksMatchesWithTitle() throws Exception {
        List<BookResponse> books = new ArrayList<>();

        when(bookService.getBooks(anyString())).thenReturn(new ResponseEntity<>(books, HttpStatus.OK));

        mockMvc.perform(get("/books/ikigai"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCallUpdateBooks() throws Exception {

        BookRequest book = new BookRequest("Ikigai", "Jane", 100, 9);

        when(bookService.updateBook(any(), any(BookRequest.class))).thenReturn(new ResponseEntity<>(new BookResponse(), HttpStatus.OK));

        mockMvc.perform(put("/books/52")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCallDeleteBook() throws Exception {
        when(bookService.deleteBook(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/books/52"))
                .andExpect(status().isOk());
    }

}