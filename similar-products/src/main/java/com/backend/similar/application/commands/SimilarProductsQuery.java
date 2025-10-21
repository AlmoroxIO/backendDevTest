package com.backend.similar.application.commands;

public record SimilarProductsQuery(

        String productId

) {
    public SimilarProductsQuery {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty.");
        }
    }
}
