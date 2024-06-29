package com.coding.crud.book.repository;

import com.coding.crud.book.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<List<BookEntity>> findByTitleContainingIgnoreCase(String title);
}
