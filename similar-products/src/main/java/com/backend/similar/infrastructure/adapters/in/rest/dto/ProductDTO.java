package com.backend.similar.infrastructure.adapters.in.rest.dto;

import java.math.BigDecimal;

public record ProductDTO(

        String id,
        String name,
        BigDecimal price,
        boolean availability

) {}
