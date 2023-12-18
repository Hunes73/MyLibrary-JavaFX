package com.library.mylibrary.data.repository;

import com.library.mylibrary.data.entity.Loan;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @NonNull Optional<Loan> findById(@NonNull Long id);

    void deleteById(@NonNull Long id);

    List<Loan> findAllByUserId(Long id);
}
