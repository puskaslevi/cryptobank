package com.blur.cryptobank.data;

import groovy.transform.EqualsAndHashCode;
import groovy.transform.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@ToString
@Setter
@Getter
@NoArgsConstructor
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
}
