package com.blur.cryptobank.repository;

import com.blur.cryptobank.data.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A JPA repository for the {@link Transaction} entity.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
