package com.blur.cryptobank.data;

import com.blur.cryptobank.data.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This is the token entity for the confirmation of the registration.
 */

@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    private LocalDateTime confirmedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public ConfirmationToken() {}

    public ConfirmationToken(String token, LocalDateTime createdDate, LocalDateTime expirationDate, UserEntity user) {
        this.token = token;
        this.createdDate = createdDate;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public LocalDateTime getConfirmedDate() {
        return confirmedDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setConfirmedDate(LocalDateTime confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
