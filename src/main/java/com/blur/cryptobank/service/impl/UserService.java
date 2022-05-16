package com.blur.cryptobank.service.impl;

import com.blur.cryptobank.data.ConfirmationToken;
import com.blur.cryptobank.data.Cryptocurrency;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.repository.UserRepository;
import com.blur.cryptobank.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private ConfirmationTokenService confirmationTokenService;

    public Optional<UserEntity> findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public String signUp(UserEntity user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent() || userRepository.findByUsername(user.getUsername()).isPresent();

        if (userExists) {
            throw new IllegalStateException("User with email " + user.getEmail() + " or with username " + user.getUsername() + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        //user.setRole(UserRole.USER);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        confirmationTokenService.save(confirmationToken);
        return token;
    }

    public void delete(UserEntity user) {
        userRepository.delete(user);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public void updateUser(UserEntity user) {
        UserEntity u = userRepository.findByUsername(user.getUsername()).get();
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());
        if(!Objects.equals(user.getPassword(), "")) {
            u.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public int enableUser(String email){
        return userRepository.enableUser(email);
    }

    public void depositFiat(String username, Double fiat){
        UserEntity u = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        u.setFiat(u.getFiat() + fiat);
        userRepository.save(u);
    }

    public void addCrypto(UserEntity user, Cryptocurrency crypto, Double amount){
        Map<Cryptocurrency, Double> map = user.getAccount();
        Double oldValue = map.get(crypto);
        if (oldValue == null)
            map.put(crypto, amount);
        else
            map.replace(crypto, oldValue + amount);

        userRepository.save(user);

    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public void lockUnlock(Long id) {
        UserEntity user = userRepository.getById(id);
        user.setLocked(!user.getLocked());
        userRepository.save(user);
    }
}
