package com.backend.entity;

import jakarta.persistence.Table;

import java.math.BigInteger;

@Table
public class Token {
    private Long id;
    private BigInteger random;


}
