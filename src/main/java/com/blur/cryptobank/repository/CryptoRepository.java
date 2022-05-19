package com.blur.cryptobank.repository;

import java.util.List;

import com.blur.cryptobank.data.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A JPA repository for the {@link Cryptocurrency} entity.
 */
public interface CryptoRepository extends JpaRepository<Cryptocurrency, Long>
{
}
