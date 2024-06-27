package com.coding.crud.book.repository;

import com.coding.crud.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<List<Book>> findByTitleContainingIgnoreCase(String title);
}
