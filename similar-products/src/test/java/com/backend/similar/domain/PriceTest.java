package com.backend.similar.domain;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

public class PriceTest {

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {

        // WHEN
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Price(null);
        });

        // THEN
        assertThat(exception.getMessage()).isEqualTo("price cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {

        // WHEN
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Price(new BigDecimal("-10.00"));
        });

        // THEN
        assertThat(exception.getMessage()).isEqualTo("amount cannot be negative");
    }
}
