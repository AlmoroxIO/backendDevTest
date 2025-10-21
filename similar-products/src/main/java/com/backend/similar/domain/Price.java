package com.backend.similar.domain;

import java.math.BigDecimal;

public record Price(

        BigDecimal amount
) {

    public Price {
        if (amount == null) {
            throw new IllegalArgumentException("price cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
    }
}
