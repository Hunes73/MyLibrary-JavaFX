package com.library.mylibrary.service;

import com.library.mylibrary.data.entity.Book;
import com.library.mylibrary.model.BookModel;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public interface BookService {

    Book createBook(BookModel bookModel);

    Book updateBook(BookModel bookModel);

    Book getBookById(Long id);

    void deleteBook(Long id);

    ObservableList<BookModel> getAllBooks();
}
