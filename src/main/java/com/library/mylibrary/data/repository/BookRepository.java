package com.library.mylibrary.data.repository;

import com.library.mylibrary.data.entity.Book;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @NonNull Optional<Book> findById(@NonNull Long id);

    void deleteById(@NonNull Long id);
}
