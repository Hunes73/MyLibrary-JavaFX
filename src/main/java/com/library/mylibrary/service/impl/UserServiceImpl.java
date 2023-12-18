package com.library.mylibrary.service.impl;

import com.library.mylibrary.data.entity.User;
import com.library.mylibrary.data.enums.Role;
import com.library.mylibrary.data.repository.UserRepository;
import com.library.mylibrary.model.BorrowerModel;
import com.library.mylibrary.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerNewUser(BorrowerModel user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword().getValue());
        User userToCreate = new User(Role.READER, user.getCardNumber().getValue(), user.getEmail().getValue(), encodedPassword, user.getFirstname().getValue(), user.getLastname().getValue());
        userToCreate.setPhone(user.getPhone().getValue());
        userRepository.save(userToCreate);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o id: " + id + "."));
    }

    @Override
    public User updateUser(BorrowerModel borrowerModel) {
        User userToUpdate = userRepository.findById(borrowerModel.getId()).orElseThrow(() -> new RuntimeException("Nie znaleziono autora o id: " + borrowerModel.getId() + "."));
        userToUpdate.setCardNumber(borrowerModel.getCardNumber().getValue());
        userToUpdate.setFirstname(borrowerModel.getFirstname().getValue());
        userToUpdate.setLastname(borrowerModel.getLastname().getValue());
        userToUpdate.setEmail(borrowerModel.getEmail().getValue());
        userToUpdate.setPhone(borrowerModel.getPhone().getValue());
        return userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public ObservableList<BorrowerModel> getAllReaders() {
        List<BorrowerModel> users = userRepository.findAll().stream().map(this::mapToModel).toList();
        return users.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o id: " + id + "."));
    }

    @Override
    public List<String> getAllCardNumbers() {
        return userRepository.findAll().stream().map(User::getCardNumber).toList();
    }

    @Override
    public String getPasswordByCardNumber(String cardNumber) {
        return userRepository.findByCardNumber(cardNumber).orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o numerze karty: " + cardNumber + ".")).getPassword();
    }

    @Override
    public User getUserByCardNumber(String cardNumber) {
        return userRepository.findByCardNumber(cardNumber).orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o numerze karty: " + cardNumber + "."));
    }

    public BorrowerModel mapToModel(User user) {
        return new BorrowerModel(user.getId(), user.getCardNumber(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPhone());
    }
}
