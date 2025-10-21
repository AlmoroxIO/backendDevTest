package com.backend.similar.infrastructure.adapters.out.rest.dto.response;

import java.math.BigDecimal;

public record ProductResponseDTO(
        String id,
        String name,
        BigDecimal price,
        boolean availability
) {}
