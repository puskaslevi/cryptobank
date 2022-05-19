package com.blur.cryptobank.service;

import com.blur.cryptobank.data.Cryptocurrency;
import com.blur.cryptobank.data.Transaction;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;


/**
 * A service for handling all the actions related to transaction process.
 */
@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;
    private UserService userService;

    private CryptoService cryptoService;

    public Transaction getTransById(long id) {
        return transactionRepository.getById(id);
    }

    /**
     * This method is used to make a transaction between two users.
     * One users' balance will be decreased and the other one will be increased.
     * Then the data of the transaction will be saved in the database.
     */
    public void makeTransaction(Transaction transaction) throws Exception{
        UserEntity sender = userService.getCurrentUser();
        transaction.setSender(sender);
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));

        Cryptocurrency currency = transaction.getCurrency();

        Double amount = transaction.getAmount();
        if (userService.decreaseBalance(currency, amount)) {
            userService.addCrypto(transaction.getReceiver(), currency, amount);
        } else
            System.out.println("You are poor");
        transactionRepository.save(transaction);
    }

    /**
     * This method is used to handle trading coins to FIAT currency or vice versa.
     *
     * @param trade This determines if a coin is to be bought or sold.
     * @param value This is the amount of the coin to be traded or the amount of the fiat currency to be traded based on the trade parameter.
     */
    public void tradeCrypto(Long id, Double value, String trade) {
        UserEntity user = userService.getCurrentUser();
        Map<Cryptocurrency, Double> account = user.getAccount();
        Cryptocurrency crypto = cryptoService.getCryptoById(id);
        if (Objects.equals(trade, "buy")) {
            userService.addCrypto(user, crypto, value/crypto.getValue());
            userService.decreaseFiatBalance(value);
        } else if (Objects.equals(trade, "sell")){
            Double oldAmount = account.get(crypto);
            userService.addCrypto(user, crypto, -value);
            userService.increaseFiatBalance(value*crypto.getValue());
        }
    }
}
