package com.backend.similar.application.usecases;

import com.backend.similar.application.commands.SimilarProductsQuery;
import com.backend.similar.application.ports.in.SimilarProductsInputPort;
import com.backend.similar.application.ports.out.SimilarProductsOutputPort;
import com.backend.similar.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SimilarProductsUseCase implements SimilarProductsInputPort {

    private final SimilarProductsOutputPort similarProductsOutputPort;

    @Override
    public List<Product> getSimilarProducts(SimilarProductsQuery similarProductsQuery) {

        log.debug("Use case started for productId: {}", similarProductsQuery.productId());

        List<String> similarProductIds;
        similarProductIds = similarProductsOutputPort.findSimilarProductIds(similarProductsQuery.productId());

        log.debug("Found {} similar product IDs for productId: {}", similarProductIds.size(), similarProductsQuery.productId());

        List<Mono<Product>> productMonos = similarProductIds.stream()
                .map(similarProductsOutputPort::findProductById)
                .toList();

        List<Product> products = Flux.merge(productMonos)
                .collectList()
                .block();

        if (products == null) {
            log.warn("Product retrieval resulted in null list for productId: {}", similarProductsQuery.productId());
            return List.of();
        }

        log.debug("Successfully fetched {} full product details for productId: {}", products.size(), similarProductsQuery.productId());

        return products;
    }
}
