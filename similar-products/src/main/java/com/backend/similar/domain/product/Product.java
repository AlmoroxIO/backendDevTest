package com.backend.similar.domain.product;

import com.backend.similar.domain.Price;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@EqualsAndHashCode(of = "productId")
public class Product {

    @Getter
    private final ProductId productId;

    @Getter
    private final ProductName productName;

    @Getter
    private final Price price;

    @Getter
    private Boolean availability;
    
    public static Product createProduct(
            ProductId productId,
            ProductName productName,
            Price price
    ){

        Objects.requireNonNull(productId, "productId cannot be null");
        Objects.requireNonNull(productName, "productName cannot be null");
        Objects.requireNonNull(price, "price cannot be null");

        return new Product(productId, productName, price, true);
    }

    public static Product rehydrate(
            ProductId productId,
            ProductName productName,
            Price price,
            Boolean availability
    ) {

        Objects.requireNonNull(productId, "productId cannot be null");
        Objects.requireNonNull(productName, "productName cannot be null");
        Objects.requireNonNull(price, "price cannot be null");
        Objects.requireNonNull(availability, "availability cannot be null");

        return new Product(productId, productName, price, availability);
    }
    
    private Product(ProductId productId, ProductName productName, Price price, Boolean availability) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.availability = availability;
    }

}
