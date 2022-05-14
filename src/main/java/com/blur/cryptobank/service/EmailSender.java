package com.blur.cryptobank.service;

public interface EmailSender {
    void send(String to, String email);
}