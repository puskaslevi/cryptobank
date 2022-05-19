package com.blur.cryptobank.data;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This is the Cryptocurrency entity.
 * There are no constructors, setters and getters implemented because is handled by Lombok with Annotations.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Cryptocurrency {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private String symbol;

    private Double value;


}
