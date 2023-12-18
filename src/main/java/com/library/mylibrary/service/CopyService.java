package com.library.mylibrary.service;

import com.library.mylibrary.data.entity.Copy;
import com.library.mylibrary.model.CopyModel;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public interface CopyService {

    Copy createCopy(CopyModel bookModel);

    Copy updateCopy(CopyModel bookModel);

    Copy getCopyById(Long id);

    void deleteCopy(Long id);

    ObservableList<CopyModel> getAllCopies();

    ObservableList<CopyModel> getAvailableCopies();
}
