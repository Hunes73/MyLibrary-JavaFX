package com.library.mylibrary.service;

import com.library.mylibrary.data.entity.User;
import com.library.mylibrary.model.BorrowerModel;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    void registerNewUser(BorrowerModel user);

    User findById(Long id);

    User updateUser(BorrowerModel borrowerModel);

    void deleteUser(Long id);

    ObservableList<BorrowerModel> getAllReaders();

    User getUserById(Long id);

    List<String> getAllCardNumbers();

    String getPasswordByCardNumber(String cardNumber);

    User getUserByCardNumber(String cardNumber);
}
