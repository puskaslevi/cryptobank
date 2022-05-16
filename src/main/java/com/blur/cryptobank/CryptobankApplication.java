package com.blur.cryptobank;

import com.blur.cryptobank.data.UserRole;
import com.blur.cryptobank.repository.CryptoRepository;
import com.blur.cryptobank.repository.UserRepository;
import com.blur.cryptobank.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CryptobankApplication {

	private static final Logger log = LoggerFactory.getLogger(CryptobankApplication.class);

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {

		SpringApplication.run(CryptobankApplication.class, args);
	}

}
