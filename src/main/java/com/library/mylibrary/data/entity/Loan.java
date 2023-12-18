package com.library.mylibrary.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "copy_id", updatable = false)
    private Copy copy;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column(name = "loan_date", nullable = false)
    @NonNull
    private LocalDate loanDate;

    @Column(name = "planned_return_date", nullable = false)
    @NonNull
    private LocalDate plannedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;
}
