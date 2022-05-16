package com.blur.cryptobank.data;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "sender_id", nullable = false)
        private UserEntity sender;
        @ManyToOne
        @JoinColumn(name = "receiver_id", nullable = false)
        private UserEntity receiver;
        private Double amount;

        @ManyToOne
        @JoinColumn(name = "cryptocurrency_id", nullable = false)
        private Cryptocurrency currency;
        private Timestamp transactionDate;


        public Transaction() {}

}
