package com.blur.cryptobank.service;

import com.blur.cryptobank.data.Cryptocurrency;

import com.blur.cryptobank.repository.CryptoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * A service for handling all the actions related to the {@link Cryptocurrency} entity.
 */
@Service
public class CryptoService {

    private CryptoRepository cryptoRepository;

    public CryptoService(CryptoRepository cryptoRepository){
        super();
        this.cryptoRepository = cryptoRepository;
    }

    public List<Cryptocurrency> getAllCrypto() {
        return cryptoRepository.findAll();
    }


    public Cryptocurrency saveCrypto(Cryptocurrency crypto) {
        return cryptoRepository.save(crypto);
    }

    public Cryptocurrency getCryptoById(Long id) {
        return cryptoRepository.findById(id).get();
    }


    public Cryptocurrency updateCrypto(Cryptocurrency crypto) {
        return cryptoRepository.save(crypto);
    }


    public void deleteCryptoById(Long id) {
        cryptoRepository.deleteById(id);
    }


    /**
     * Returns a filtered list of {@link Cryptocurrency} objects.
     * @param symbol the symbol filter
     * @param name the name of filter
     * @return a filtered list of {@link Cryptocurrency} objects
     */
    public List<Cryptocurrency> getAllCryptoFilter(String symbol, String name) {
        List<Cryptocurrency> cryptos = cryptoRepository.findAll();
        List<Cryptocurrency> results = new ArrayList<>();
        for (Cryptocurrency crypto: cryptos) {
            if (crypto.getSymbol().contains(symbol) && crypto.getName().contains(name)){
                results.add(crypto);
            }
        }
        return results;
    }
}
