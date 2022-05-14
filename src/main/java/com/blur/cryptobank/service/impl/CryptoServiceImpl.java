package com.blur.cryptobank.service.impl;

import com.blur.cryptobank.data.Cryptocurrency;
import com.blur.cryptobank.repository.CryptoRepository;
import com.blur.cryptobank.service.CryptoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoServiceImpl implements CryptoService {

    private CryptoRepository cryptoRepository;

    public CryptoServiceImpl(CryptoRepository cryptoRepository){
        super();
        this.cryptoRepository = cryptoRepository;
    }

    @Override
    public List<Cryptocurrency> getAllCrypto() {
        return cryptoRepository.findAll();
    }

    @Override
    public Cryptocurrency saveCrypto(Cryptocurrency crypto) {
        return cryptoRepository.save(crypto);
    }

    @Override
    public Cryptocurrency getCryptoById(Long id) {
        return cryptoRepository.findById(id).get();
    }

    @Override
    public Cryptocurrency updateCrypto(Cryptocurrency crypto) {
        return cryptoRepository.save(crypto);
    }

    @Override
    public void deleteCryptoById(Long id) {
        cryptoRepository.deleteById(id);
    }
}
