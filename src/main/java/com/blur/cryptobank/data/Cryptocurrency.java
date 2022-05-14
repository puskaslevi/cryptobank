package com.blur.cryptobank.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cryptocurrency {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private String symbol;

    private Double value;

    public Cryptocurrency() {}

    public Cryptocurrency(String name, String symbol, Double value) {
        super();
        this.name = name;
        this.symbol = symbol;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Cryptocurrency[id=%d, name='%s', value='%s']", id, name, value);
    }

    public Double getValue() {
        return value;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
