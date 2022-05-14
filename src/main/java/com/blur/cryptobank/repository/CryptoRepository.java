package com.blur.cryptobank.repository;

import java.util.List;

import com.blur.cryptobank.data.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoRepository extends JpaRepository<Cryptocurrency, Long>
{
}
