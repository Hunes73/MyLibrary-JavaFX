package com.library.mylibrary.service;

import com.library.mylibrary.data.entity.Loan;
import com.library.mylibrary.model.LoanModel;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface LoanService {

    Loan createLoan(LoanModel loan);

    Loan updateLoan(LoanModel loan);

    Loan getLoanById(Long id);

    void deleteLoan(Long id);

    ObservableList<LoanModel> getAllLoans();

    ObservableList<LoanModel> getAllLoansByUser(Long id);

    void returnBook(Long value) throws IOException;
}
