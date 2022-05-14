package com.blur.cryptobank.service;

import com.blur.cryptobank.data.Cryptocurrency;

import java.util.List;

public interface CryptoService {
    List<Cryptocurrency> getAllCrypto();

    Cryptocurrency saveCrypto(Cryptocurrency crypto);

    Cryptocurrency getCryptoById(Long id);

    Cryptocurrency updateCrypto(Cryptocurrency crypto);

    void deleteCryptoById(Long id);
}
