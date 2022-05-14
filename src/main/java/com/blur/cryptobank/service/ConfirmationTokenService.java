package com.blur.cryptobank.service;

import com.blur.cryptobank.data.ConfirmationToken;
import com.blur.cryptobank.repository.ConfirmationTokenRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void save(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int confirmToken(String token) {
        return confirmationTokenRepository.confirmToken(token, LocalDateTime.now());
    }

}
