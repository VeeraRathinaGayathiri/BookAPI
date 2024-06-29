package com.coding.crud.book.repository;

import com.coding.crud.book.model.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;
@ExtendWith(MockitoExtension.class)
@DataJpaTest
class BookEntityRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    public void setUp(){
        bookRepository.save(new BookEntity(1L, "Anna", "Anna", 100, 10));
        bookRepository.save(new BookEntity(2L, "Ansa", "Ansa", 200, 10));
    }


    @Test
    public void TestCreateBook(){
        BookEntity newBook = new BookEntity(3L, "Mishi", "Mishi", 300, 30);
        BookEntity result = bookRepository.save(newBook);
        assertAll(() -> assertNotNull(result),
                () -> assertEquals("Mishi", result.getTitle()));
    }

    @Test
    public void TestFetchAllBooks(){
        List<BookEntity> books = bookRepository.findAll();
        assertAll(() -> assertNotNull(books),
                () -> assertEquals(2, books.size()));
    }

    @Test
    public void TestFetchTitleMatch() {
        Optional<List<BookEntity>> books = bookRepository.findByTitleContainingIgnoreCase("an");

        assertAll(() -> assertNotNull(books),
                () -> assertTrue(books.isPresent()));
        List<String> titles = books.get().stream().map(x -> x.getTitle()).collect(Collectors.toList());
        assertTrue(titles.stream().allMatch(x -> x.contains("An")));
    }

    @Test
    public void TestFetchByID() {
        Optional<BookEntity> result = bookRepository.findById(2L);

        assertAll(() -> assertNotNull(result),
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(Optional.of(2L) , Optional.of(result.get().getId())));
    }

    @Test
    public void TestDeleteBook() {
        bookRepository.deleteById(1L);
        Optional<BookEntity> result = bookRepository.findById(1L);
        assertFalse(result.isPresent());
    }
}