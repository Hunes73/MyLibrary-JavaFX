package com.library.mylibrary.data.repository;

import com.library.mylibrary.data.entity.Copy;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {

    @NonNull Optional<Copy> findById(@NonNull Long id);

    void deleteById(@NonNull Long id);
}
