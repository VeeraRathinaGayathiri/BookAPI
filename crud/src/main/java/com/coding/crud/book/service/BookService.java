package com.coding.crud.book.service;

import com.coding.crud.book.dto.BookRequest;
import com.coding.crud.book.dto.BookResponse;
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

    public ResponseEntity<BookResponse> createBook(BookRequest book) {
        Book newBook = mapToDto(book);
        try{
            BookResponse response = mapToResponse(bookRepository.save(newBook));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    This method returns a list of book if title not specified.
    If title provided - return the book with exact match
    This can be further extended by returning books closely matches with the title provided.
     */
    public ResponseEntity<List<BookResponse>> getBooks(String title) {
        List<Book> books = new ArrayList<>();
        List<BookResponse> response = new ArrayList<>();

        try {
            if (title == null) {
                bookRepository.findAll().forEach(books::add);
                response = books.stream().map(this::mapToResponse).toList();
            } else {
                Optional<List<Book>> booksData = bookRepository.findByTitleContainingIgnoreCase(title);
                if(booksData.isPresent()){
                    booksData.get().forEach(books::add);
                    response = books.stream().map(this::mapToResponse).toList();
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<BookResponse> updateBook(Long id, BookRequest book) {
       Optional<Book> bookData = bookRepository.findById(id);
       try {
           if (bookData.isPresent()) { //if data present in db update details
               Book _book = Book.builder()
                       .id(bookData.get().getId())
                       .title(book.getTitle())
                       .author(book.getAuthor())
                       .price(book.getPrice())
                       .quantity(book.getQuantity())
                       .build();
               BookResponse response = mapToResponse(bookRepository.save(_book));
               return new ResponseEntity<>(response, HttpStatus.OK);
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

    private BookResponse mapToResponse(Book book){
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .quantity(book.getQuantity())
                .build();
    }

    private Book mapToDto(BookRequest book){
        return Book.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .quantity(book.getQuantity())
                .build();
    }
}
