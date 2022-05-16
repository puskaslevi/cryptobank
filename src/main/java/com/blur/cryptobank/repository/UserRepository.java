package com.blur.cryptobank.repository;

import com.blur.cryptobank.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);

}
