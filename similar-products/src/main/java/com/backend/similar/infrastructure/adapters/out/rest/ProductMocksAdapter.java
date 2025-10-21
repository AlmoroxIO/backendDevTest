package com.backend.similar.infrastructure.adapters.out.rest;

import com.backend.similar.application.exceptions.ExternalServiceException;
import com.backend.similar.application.exceptions.ProductNotFoundException;
import com.backend.similar.application.ports.out.SimilarProductsOutputPort;
import com.backend.similar.domain.Price;
import com.backend.similar.domain.product.Product;
import com.backend.similar.domain.product.ProductId;
import com.backend.similar.domain.product.ProductName;
import com.backend.similar.infrastructure.adapters.out.rest.dto.response.ProductResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class ProductMocksAdapter implements SimilarProductsOutputPort {

    private final WebClient webClient;

    public ProductMocksAdapter(WebClient.Builder webClientBuilder, @Value("${mocks.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public List<String> findSimilarProductIds(String productId) {

        log.debug("Fetching similar product IDs for productId: {}", productId);

        return webClient.get()
                .uri("/product/{productId}/similarids", productId)
                .retrieve()
                .onStatus(
                        status -> status.equals(HttpStatus.NOT_FOUND),
                        clientResponse -> {
                            log.warn("Main product not found (404) for productId: {}", productId);
                            return Mono.error(new ProductNotFoundException("Product not found: " + productId));
                        }
                )
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .timeout(Duration.ofSeconds(3))
                .onErrorMap(
                        ex -> !(ex instanceof ProductNotFoundException), // No envolver la que ya lanzamos
                        ex -> {
                            log.error("Error fetching similar IDs for productId: {}", productId, ex);
                            return new ExternalServiceException("External service error while fetching similar IDs: " + ex.getMessage(), ex);
                        }
                )
                .block();
    }

    @Override
    public Mono<Product> findProductById(String productId) {

        log.debug("Fetching product detail for productId: {}", productId);

        return webClient.get()
                .uri("/product/{productId}", productId)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> {
                            log.warn("Error response for productId: {} - Status: {}", productId, clientResponse.statusCode());
                            return Mono.empty();
                        }
                )
                .bodyToMono(ProductResponseDTO.class)
                .map(this::mapToDomain)
                .timeout(Duration.ofSeconds(2))
                .onErrorResume(TimeoutException.class, ex -> {
                    log.warn("Timeout fetching product detail for productId: {}", productId);
                    return Mono.empty();
                })
                .onErrorResume(WebClientException.class, ex -> {
                    log.warn("WebClient error fetching product detail for productId: {}: {}", productId, ex.getMessage());
                    return Mono.empty();
                })
                .doOnError(error -> log.error("Error fetching product detail for productId: {}", productId, error));
    }

    private Product mapToDomain(ProductResponseDTO dto) {
        return Product.rehydrate(
                new ProductId(dto.id()),
                new ProductName(dto.name()),
                new Price(dto.price()),
                dto.availability()
        );
    }
}
