package com.backend.similar.domain.product;

public record ProductName(

        String name

) {
    public ProductName {
        if (name==null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("name cannot be less than 3 characters");
        }
        if (name.length() > 30) {
            throw new IllegalArgumentException("name cannot be more than 30 characters");
        }
    }
}
