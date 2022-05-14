package com.blur.cryptobank.service.impl;

import com.blur.cryptobank.data.Transaction;
import com.blur.cryptobank.repository.TransactionRepository;
import com.blur.cryptobank.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;
    @Override
    public Transaction getTransById(long id) {
        return transactionRepository.getById(id);
    }
}
