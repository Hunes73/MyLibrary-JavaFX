package com.library.mylibrary.data.entity;

import com.library.mylibrary.data.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NonNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true, nullable = false)
    @NonNull
    private String cardNumber;

    @Column(unique = true, nullable = false)
    @NonNull
    private String email;

    private String phone;

    @Column
    @NonNull
    private String password;

    @Column(nullable = false)
    @NonNull
    private String firstname;

    @Column(nullable = false)
    @NonNull
    private String lastname;

    @OneToMany(mappedBy = "user")
    private List<Loan> loans;
}
