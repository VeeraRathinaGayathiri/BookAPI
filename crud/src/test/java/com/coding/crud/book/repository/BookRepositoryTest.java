package com.coding.crud.book.repository;

import com.coding.crud.book.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;
@ExtendWith(MockitoExtension.class)
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    public void setUp(){
        bookRepository.save(new Book(1L, "Anna", "Anna", 100, 10));
        bookRepository.save(new Book(2L, "Ansa", "Ansa", 200, 10));
    }


    @Test
    public void TestCreateBook(){
        Book newBook = new Book(3L, "Mishi", "Mishi", 300, 30);
        Book result = bookRepository.save(newBook);
        assertAll(() -> assertNotNull(result),
                () -> assertEquals("Mishi", result.getTitle()));
    }

    @Test
    public void TestFetchAllBooks(){
        List<Book> books = bookRepository.findAll();
        assertAll(() -> assertNotNull(books),
                () -> assertEquals(2, books.size()));
    }

    @Test
    public void TestFetchTitleMatch() {
        Optional<List<Book>> books = bookRepository.findByTitleContainingIgnoreCase("an");
        assertAll(() -> assertNotNull(books),
                () -> assertTrue(books.isPresent()),
                () -> assertEquals(2, books.get().size()));
    }

    @Test
    public void TestFetchByID() {
        Optional<Book> result = bookRepository.findById(2L);

        assertAll(() -> assertNotNull(result),
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(Optional.of(2L) , Optional.of(result.get().getId())));
    }

    @Test
    public void TestDeleteBook() {
        bookRepository.deleteById(1L);
        Optional<Book> result = bookRepository.findById(1L);
        assertFalse(result.isPresent());
    }
}