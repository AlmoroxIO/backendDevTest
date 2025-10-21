package com.backend.similar.application.ports.out;

import com.backend.similar.domain.product.Product;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SimilarProductsOutputPort {

    List<String> findSimilarProductIds(String productId);

    Mono<Product> findProductById(String productId);

}
