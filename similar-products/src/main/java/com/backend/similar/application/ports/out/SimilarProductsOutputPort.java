package com.backend.similar.application.ports.out;

import com.backend.similar.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface SimilarProductsOutputPort {

    List<String> findSimilarProductIds(String productId);

    Optional<Product> findProductById(String productId);

}
