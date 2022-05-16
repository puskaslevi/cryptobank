package com.blur.cryptobank.service.impl;

import com.blur.cryptobank.data.Transaction;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;
    private UserService userService;

    public Transaction getTransById(long id) {
        return transactionRepository.getById(id);
    }

    public void makeTransaction(Transaction transaction){
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        userService.addCrypto(transaction.getReceiver(), transaction.getCurrency(), transaction.getAmount());

        transactionRepository.save(transaction);
    }
}
