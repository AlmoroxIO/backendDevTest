package com.backend.similar.domain.product;

public record ProductId(

        String id

) {
    public ProductId {
        if(id==null) {
            throw new IllegalArgumentException("id cannot be null");
        }
    }
}
