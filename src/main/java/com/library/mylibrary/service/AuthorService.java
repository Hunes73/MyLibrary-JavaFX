package com.library.mylibrary.service;

import com.library.mylibrary.data.entity.Author;
import com.library.mylibrary.model.AuthorModel;
import jakarta.transaction.Transactional;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public interface AuthorService {
    Author createAuthor(AuthorModel authorModel);

    Author updateAuthor(AuthorModel author);

    void deleteAuthor(Long id);

    ObservableList<AuthorModel> getAllAuthors();

    @Transactional
    Author getAuthorById(Long id);
}
