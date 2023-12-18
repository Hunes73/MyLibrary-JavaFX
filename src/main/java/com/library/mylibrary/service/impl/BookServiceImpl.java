package com.library.mylibrary.service.impl;

import com.library.mylibrary.data.entity.Author;
import com.library.mylibrary.data.entity.Book;
import com.library.mylibrary.data.repository.AuthorRepository;
import com.library.mylibrary.data.repository.BookRepository;
import com.library.mylibrary.model.BookModel;
import com.library.mylibrary.service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override

    public Book createBook(BookModel bookModel) {
        Book bookToCreate = new Book(bookModel.getTitle().getValue(), bookModel.getGenre().getValue(), bookModel.getDescription().getValue(), bookModel.getAuthors());
        Book createdBook = bookRepository.save(bookToCreate);
        List<Author> authors = bookModel.getAuthors();
        for (Author author : authors) {
            if (!author.getBooks().contains(bookToCreate)) {
                author.getBooks().add(bookToCreate);
                authorRepository.save(author);
            }
        }
        return createdBook;
    }

    @Override
    public Book updateBook(BookModel bookModel) {
        Book bookToUpdate = bookRepository.findById(bookModel.getId()).orElseThrow(() -> new RuntimeException("Nie znaleziono książki o id: " + bookModel.getId() + "."));
        List<Author> authors = bookModel.getAuthors();
        for (Author author : authors) {
            if (!author.getBooks().contains(bookToUpdate)) {
                author.getBooks().add(bookToUpdate);
                authorRepository.save(author);
            }
        }
        bookToUpdate.setTitle(bookModel.getTitle().getValue());
        bookToUpdate.setGenre(bookModel.getGenre().getValue());
        bookToUpdate.setDescription(bookModel.getDescription().getValue());
        bookToUpdate.setAuthors(bookModel.getAuthors());

        return bookRepository.save(bookToUpdate);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono książki o id: " + id + "."));
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public ObservableList<BookModel> getAllBooks() {
        List<BookModel> books = bookRepository.findAll().stream().map(this::mapToModel).toList();
        return books.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public BookModel mapToModel(Book book) {
        return new BookModel(book.getId(), book.getTitle(), book.getGenre(), book.getDescription(), book.getAuthors());
    }
}
