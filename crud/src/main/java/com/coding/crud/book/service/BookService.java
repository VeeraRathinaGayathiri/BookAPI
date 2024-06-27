package com.coding.crud.book.service;

import com.coding.crud.book.model.Book;
import com.coding.crud.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {


    @Autowired
    BookRepository bookRepository;

    public ResponseEntity<Book> createBook(Book book) {
        try{
            Book _book = bookRepository.save(new Book(book.getTitle(), book.getAuthor(),book.getPrice(), book.getQuantity()));
            return new ResponseEntity<>(_book, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    This method returns a list of book if title not specified.
    If title provided - return the book with exact match
    This can be further extended by returning books closely matches with the title provided.
     */
    public ResponseEntity<List<Book>> getBooks(String title) {
        List<Book> books = new ArrayList<>();

        try {
            if (title == null) {
                bookRepository.findAll().forEach(books::add);
            } else {
                Optional<List<Book>> booksData = bookRepository.findByTitleContainingIgnoreCase(title);
                if(booksData.isPresent()){
                    booksData.get().forEach(books::add);
                    return new ResponseEntity<>(books, HttpStatus.OK);
                }
            }
            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(books, HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Book> updateBook(Long id, Book book) {
       Optional<Book> bookData = bookRepository.findById(id);
       try {
           if (bookData.isPresent()) { //if data present in db update details
               Book _book = bookData.get();
               _book.setTitle(book.getTitle());
               _book.setAuthor(book.getAuthor());
               _book.setPrice(book.getPrice());
               _book.setQuantity(book.getQuantity());
               return new ResponseEntity<>(bookRepository.save(_book), HttpStatus.OK);
           } else { //if data not present in db insert a new record
               return createBook(book);
           }
       }catch (Exception e){
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    public ResponseEntity<HttpStatus> deleteBook(Long id) {
        Optional<Book> bookData = bookRepository.findById(id);
        try {
            if (bookData.isPresent()) {
                bookRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
