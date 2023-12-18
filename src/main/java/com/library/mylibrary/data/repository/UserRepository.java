package com.library.mylibrary.data.repository;

import com.library.mylibrary.data.entity.User;
import com.library.mylibrary.data.enums.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull Optional<User> findByCardNumber(@NonNull String cardNumber);

    void deleteById(@NonNull Long id);

    List<User> findByRole(Role role);
}
