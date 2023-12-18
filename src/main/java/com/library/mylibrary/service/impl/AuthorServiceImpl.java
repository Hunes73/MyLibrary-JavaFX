package com.library.mylibrary.service.impl;

import com.library.mylibrary.data.entity.Author;
import com.library.mylibrary.data.repository.AuthorRepository;
import com.library.mylibrary.model.AuthorModel;
import com.library.mylibrary.service.AuthorService;
import jakarta.transaction.Transactional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author createAuthor(AuthorModel authorModel) {
        Author authorToCreate = new Author(authorModel.getFirstname().getValue(), authorModel.getLastname().getValue(),
                authorModel.getYearOfBirth().getValue(), authorModel.getCountry().getValue());
        return authorRepository.save(authorToCreate);
    }

    @Override
    public Author updateAuthor(AuthorModel authorModel) {
        Author authorToUpdate = authorRepository.findById(authorModel.getId()).orElseThrow(() -> new RuntimeException("Nie znaleziono autora o id: " + authorModel.getId() + "."));
        authorToUpdate.setFirstname(authorModel.getFirstname().getValue());
        authorToUpdate.setLastname(authorModel.getLastname().getValue());
        authorToUpdate.setYearOfBirth(authorModel.getYearOfBirth().getValue());
        authorToUpdate.setCountry(authorModel.getCountry().getValue());
        return authorRepository.save(authorToUpdate);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public ObservableList<AuthorModel> getAllAuthors() {
        List<AuthorModel> authors = authorRepository.findAll().stream().map(this::mapToModel).toList();
        return authors.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    @Transactional
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono autora o id: " + id + "."));
    }

    public AuthorModel mapToModel(Author author) {
        return new AuthorModel(author.getId(), author.getFirstname(), author.getLastname(), author.getYearOfBirth(), author.getCountry());
    }
}
