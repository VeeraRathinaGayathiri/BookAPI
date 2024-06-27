package com.coding.crud.book.controller;


import com.coding.crud.book.dto.BookRequest;
import com.coding.crud.book.dto.BookResponse;
import com.coding.crud.book.repository.BookRepository;
import com.coding.crud.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    //Create Method
    @PostMapping()
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest book) {
        return bookService.createBook(book);
    }

    //Read all Books
    @GetMapping()
    public ResponseEntity<List<BookResponse>> getBooks(){
        return bookService.getBooks(null);
    }

    //Read specific book by title
    @GetMapping("/{title}")
    public ResponseEntity<List<BookResponse>> getBooks(@PathVariable String title){
        return bookService.getBooks(title);
    }

    //Update Method
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable("id") Long id, @RequestBody BookRequest book){
        return bookService.updateBook(id, book);
    }

    //Delete Method
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") Long id){
        return bookService.deleteBook(id);
    }

}
