package com.blur.cryptobank.service.impl;

import com.blur.cryptobank.data.ConfirmationToken;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.data.UserRole;
import com.blur.cryptobank.repository.UserRepository;
import com.blur.cryptobank.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private ConfirmationTokenService confirmationTokenService;


    public String signUp(UserEntity user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (userExists) {
            throw new IllegalStateException("User with email " + user.getEmail() + " already exists");
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

    public UserEntity updateUser(UserEntity user) {
        return userRepository.save(user);
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
}
