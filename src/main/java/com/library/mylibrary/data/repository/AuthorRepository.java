package com.library.mylibrary.data.repository;

import com.library.mylibrary.data.entity.Author;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @NonNull Optional<Author> findById(@NonNull Long id);

    void deleteById(@NonNull Long id);
}
