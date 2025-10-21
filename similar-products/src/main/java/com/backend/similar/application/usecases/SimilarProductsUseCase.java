package com.backend.similar.application.usecases;

import com.backend.similar.application.commands.SimilarProductsQuery;
import com.backend.similar.application.ports.in.SimilarProductsInputPort;
import com.backend.similar.application.ports.out.SimilarProductsOutputPort;
import com.backend.similar.domain.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

        List<Product> products = similarProductIds.stream()
                .parallel()
                .map(similarProductsOutputPort::findProductById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        log.debug("Successfully fetched {} full product details for productId: {}", products.size(), similarProductsQuery.productId());

        return products;
    }
}
