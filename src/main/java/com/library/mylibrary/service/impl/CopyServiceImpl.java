package com.library.mylibrary.service.impl;

import com.library.mylibrary.data.entity.Book;
import com.library.mylibrary.data.entity.Copy;
import com.library.mylibrary.data.enums.Status;
import com.library.mylibrary.data.repository.BookRepository;
import com.library.mylibrary.data.repository.CopyRepository;
import com.library.mylibrary.model.CopyModel;
import com.library.mylibrary.service.CopyService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CopyServiceImpl implements CopyService {

    private final CopyRepository copyRepository;

    private final BookRepository bookRepository;

    public CopyServiceImpl(CopyRepository copyRepository, BookRepository bookRepository) {
        this.copyRepository = copyRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Copy createCopy(CopyModel bookModel) {
        Copy copyToCreate = new Copy(bookModel.getBook(), bookModel.getIsbn().getValue(), bookModel.getYearOfPublication().getValue(), bookModel.getPublisher().getValue(), bookModel.getLanguage().getValue(), Status.fromString(bookModel.getStatus().getValue()));
        Book book = bookRepository.findById(bookModel.getBook().getId()).orElseThrow(() -> new RuntimeException("Nie znaleziono książki o id: " + bookModel.getBook().getId() + "."));
        book.getCopies().add(copyToCreate);
        bookRepository.save(book);
        return copyRepository.save(copyToCreate);
    }

    @Override
    public Copy updateCopy(CopyModel bookModel) {
        Copy copyToUpdate = copyRepository.findById(bookModel.getId()).orElseThrow(() -> new RuntimeException("Nie znaleziono książki o id: " + bookModel.getId() + "."));
        Book book = bookRepository.findById(bookModel.getBook().getId()).orElseThrow(() -> new RuntimeException("Nie znaleziono książki o id: " + bookModel.getBook().getId() + "."));
        copyToUpdate.setBook(book);
        copyToUpdate.setIsbn(bookModel.getIsbn().getValue());
        copyToUpdate.setYearOfPublication(bookModel.getYearOfPublication().getValue());
        copyToUpdate.setPublisher(bookModel.getPublisher().getValue());
        copyToUpdate.setLanguage(bookModel.getLanguage().getValue());
        copyToUpdate.setStatus(Status.fromString(bookModel.getStatus().getValue()));
        return copyRepository.save(copyToUpdate);
    }

    @Override
    public Copy getCopyById(Long id) {
        return copyRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono książki o id: " + id + "."));
    }

    @Override
    public void deleteCopy(Long id) {
        copyRepository.deleteById(id);
    }

    @Override
    public ObservableList<CopyModel> getAllCopies() {
        List<CopyModel> copies = copyRepository.findAll().stream().map(this::mapToModel).toList();
        return copies.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public ObservableList<CopyModel> getAvailableCopies() {
        List<CopyModel> copies = copyRepository.findAll().stream().filter(copy -> copy.getStatus().equals(Status.AVAILABLE)).map(this::mapToModel).toList();
        return copies.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public CopyModel mapToModel(Copy copy) {
        return new CopyModel(copy.getId(), copy.getBook(), copy.getYearOfPublication(), copy.getIsbn(), copy.getPublisher(), copy.getLanguage(), copy.getStatus().toString());
    }
}
