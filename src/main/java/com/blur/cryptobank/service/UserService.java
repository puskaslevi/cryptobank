package com.blur.cryptobank.service;

import com.blur.cryptobank.data.ConfirmationToken;
import com.blur.cryptobank.data.Cryptocurrency;
import com.blur.cryptobank.data.UserEntity;
import com.blur.cryptobank.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


/**
 * A service for handling all the actions related to the {@link UserEntity} entity.
 * It implements the {@link UserDetailsService} interface to be able to use the Spring Security features for authentication.
 */
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private ConfirmationTokenService confirmationTokenService;

    public Optional<UserEntity> findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }


    /**
     * This method is used to register a new user.
     * Encodes the password and saves the user in the database.
     * Checks if the user already exists in the database.
     * If the user already exists, it throws an exception.
     * If the user does not exist, it creates a new confirmation token and sends the confirmation email to the user's email address.
     * @param user the user entity to be saved
     * @return the confirmation token
     */
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

    /**
     * This method is required by the {@link UserDetailsService} interface to be able to use the UserEntity information for authentication.
     * @param username the username of the user to be logged in
     * @return a UserDetials entity containing the user information
     */
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

    /**
     * This method is used deposit funds to the user's account.
     * @param fiat the amount of fiat to be deposited
     */
    public void depositFiat(Double fiat){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity u = userRepository.findByUsername(authentication.getName()).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        u.setFiat(u.getFiat() + fiat);
        userRepository.save(u);
    }


    /**
     * This method is used to add a coin to the user's account.
     * @param user the user to be updated
     * @param crypto the type of cryptocurrency to be added
     * @param amount the amount of cryptocurrency to be added
     */
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

    /**
     * Return the currently logged in user entity.
     * @return user entity
     */
    public UserEntity getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return findUserByUserName(authentication.getName()).get();
    }

    /**
     * Method used to decrease the user's account balance.
     * @return boolean value indicating if the user's account balance was decreased successfully
     */
    public boolean decreaseBalance(Cryptocurrency crypto, Double value){
        UserEntity user = getCurrentUser();

        Map<Cryptocurrency, Double> map = user.getAccount();
        Double currentBalance = map.get(crypto);
        if (currentBalance < value)
            return false;
        else {
            map.replace(crypto, currentBalance-value);
            return true;
        }

    }


    public void decreaseFiatBalance(Double value){
        UserEntity user = getCurrentUser();
        user.setFiat(user.getFiat()-value);
        userRepository.save(user);
    }

    public void increaseFiatBalance(Double value){
        UserEntity user = getCurrentUser();
        user.setFiat(user.getFiat()+value);
        userRepository.save(user);
    }


}
