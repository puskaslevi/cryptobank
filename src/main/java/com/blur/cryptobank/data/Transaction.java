package com.blur.cryptobank.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        private Long senderId;
        private Long receiverId;
        private Double amount;
        private Long currencyId;
        private Timestamp transactionDate;


        protected Transaction() {}

        public Transaction(Long senderId, Long receiverId, Double amount, Long currencyId, Timestamp transactionDate) {
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.amount = amount;
            this.currencyId = currencyId;
            this.transactionDate = transactionDate;
        }

        public Long getId() {
            return id;
        }

        public Long getSenderId() {
            return senderId;
        }

        public Long getReceiverId() {
            return receiverId;
        }

        public Double getAmount() {
            return amount;
        }

        public Long getCurrencyId() {
            return currencyId;
        }

        public Date getTransactionDate() {
            return transactionDate;
        }

        @Override
        public String toString() {
            return String.format("Transaction[id=%d, senderId='%d', receiverId='%d', amount='%f', currencyId='%d', transactionDate='%s']", id, senderId, receiverId, amount, currencyId, transactionDate);
        }   
}
