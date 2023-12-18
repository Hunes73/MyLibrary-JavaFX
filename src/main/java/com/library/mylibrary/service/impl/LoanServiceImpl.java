package com.library.mylibrary.service.impl;

import com.library.mylibrary.controller.MainController;
import com.library.mylibrary.data.entity.Copy;
import com.library.mylibrary.data.entity.Loan;
import com.library.mylibrary.data.entity.User;
import com.library.mylibrary.data.enums.Status;
import com.library.mylibrary.data.repository.CopyRepository;
import com.library.mylibrary.data.repository.LoanRepository;
import com.library.mylibrary.data.repository.UserRepository;
import com.library.mylibrary.model.LoanModel;
import com.library.mylibrary.service.LoanService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    private final MainController mainController;

    private final LoanRepository loanRepository;

    private final CopyRepository copyRepository;

    private final UserRepository userRepository;

    public LoanServiceImpl(MainController mainController, LoanRepository loanRepository, CopyRepository copyRepository,
                           UserRepository userRepository) {
        this.mainController = mainController;
        this.loanRepository = loanRepository;
        this.copyRepository = copyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Loan createLoan(LoanModel loan) {
        Loan loanToCreate = new Loan(loan.getCopy(), loan.getUser(), loan.getLoanDate(), loan.getPlannedReturnDate());
        loanToCreate.setActualReturnDate(loan.getActualReturnDate());
        return loanRepository.save(loanToCreate);
    }

    @Override
    public Loan updateLoan(LoanModel loan) {
        Loan loanToUpdate = loanRepository.findById(loan.getId().getValue()).orElseThrow(() -> new RuntimeException("Nie znaleziono wypożyczenia o id: " + loan.getId() + "."));
        Copy copy = copyRepository.findById(loan.getCopy().getId()).orElseThrow(() -> new RuntimeException("Nie znaleziono egzemplarza o id: " + loan.getCopy().getId() + "."));
        User user = userRepository.findById(loan.getUser().getId()).orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o id: " + loan.getUser().getId() + "."));
        loanToUpdate.setCopy(copy);
        loanToUpdate.setUser(user);
        loanToUpdate.setLoanDate(loan.getLoanDate());
        loanToUpdate.setPlannedReturnDate(loan.getPlannedReturnDate());
        loanToUpdate.setActualReturnDate(loan.getActualReturnDate());
        return loanRepository.save(loanToUpdate);
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono wypożyczenia o id: " + id + "."));
    }

    @Override
    public void deleteLoan(Long id) {
        Loan loanToDelete = loanRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono wypożyczenia o id: " + id + "."));
        loanToDelete.getCopy().setStatus(Status.AVAILABLE);
        loanRepository.deleteById(id);
    }

    @Override
    public ObservableList<LoanModel> getAllLoans() {
        List<LoanModel> loans = loanRepository.findAll().stream().map(this::mapToModel).toList();
        return loans.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public ObservableList<LoanModel> getAllLoansByUser(Long id) {
        List<LoanModel> loans = loanRepository.findAllByUserId(id).stream().map(this::mapToModel).toList();
        return loans.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public void returnBook(Long value) throws IOException {

        Loan loan = loanRepository.findById(value).orElseThrow(() -> new RuntimeException("Nie znaleziono wypożyczenia o id: " + value + "."));
        if (loan.getCopy().getStatus().equals(Status.AVAILABLE) || loan.getActualReturnDate() == null) {
            mainController.showPopup("Książka jest już zwrócona.");
            return;
        }
        loan.setActualReturnDate(LocalDate.now());
        loan.getCopy().setStatus(Status.AVAILABLE);
        if (loan.getActualReturnDate().isAfter(loan.getPlannedReturnDate())) {
            mainController.showPopup("Kara za przetrzymanie książki wynosi: " + ChronoUnit.DAYS.between(loan.getPlannedReturnDate(), loan.getActualReturnDate()) + " zł.");
        }
        else {
            mainController.showPopup("Książka została zwrócona pomyślnie.");
        }
        loanRepository.save(loan);
    }

    private LoanModel mapToModel(Loan loan) {
        return new LoanModel(loan.getId(), loan.getCopy(), loan.getUser(), loan.getCopy().getBook().getTitle(),
                loan.getUser().getCardNumber(), loan.getUser().getFirstname() + " " + loan.getUser().getLastname(),
                loan.getLoanDate(), loan.getPlannedReturnDate(), loan.getActualReturnDate());
    }
}
