package com.backend.similar.application.usecases;

import com.backend.similar.application.commands.SimilarProductsQuery;
import com.backend.similar.application.exceptions.ProductNotFoundException;
import com.backend.similar.application.ports.out.SimilarProductsOutputPort;
import com.backend.similar.domain.Price;
import com.backend.similar.domain.product.Product;
import com.backend.similar.domain.product.ProductId;
import com.backend.similar.domain.product.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SimilarProductsUseCaseTest {

    @Mock
    private SimilarProductsOutputPort similarProductsOutputPort;

    private SimilarProductsUseCase similarProductsUseCase;

    @BeforeEach
    void setUp() {
        similarProductsUseCase = new SimilarProductsUseCase(similarProductsOutputPort);
    }

    @Test
    void shouldReturnSimilarProductsSuccessfully() {
        // GIVEN
        SimilarProductsQuery query = new SimilarProductsQuery("1");

        // Data
        Product product2 = Product.rehydrate(new ProductId("2"), new ProductName("Shirt"), new Price(BigDecimal.TEN), true);
        Product product3 = Product.rehydrate(new ProductId("3"), new ProductName("Pants"), new Price(BigDecimal.ONE), true);

        when(similarProductsOutputPort.findSimilarProductIds("1")).thenReturn(List.of("2", "3"));
        when(similarProductsOutputPort.findProductById("2")).thenReturn(Optional.of(product2));
        when(similarProductsOutputPort.findProductById("3")).thenReturn(Optional.of(product3));

        // WHEN
        List<Product> result = similarProductsUseCase.getSimilarProducts(query);

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(product2, product3);

        // Verify
        verify(similarProductsOutputPort).findSimilarProductIds("1");
        verify(similarProductsOutputPort, times(2)).findProductById(anyString());
    }

    @Test
    void shouldFilterProductsThatAreNotFound() {

        // GIVEN
        SimilarProductsQuery query = new SimilarProductsQuery("1");
        Product product2 = Product.rehydrate(new ProductId("2"), new ProductName("Shirt"), new Price(BigDecimal.TEN), true);

        when(similarProductsOutputPort.findSimilarProductIds("1")).thenReturn(List.of("2", "99"));
        when(similarProductsOutputPort.findProductById("2")).thenReturn(Optional.of(product2));
        when(similarProductsOutputPort.findProductById("99")).thenReturn(Optional.empty());

        // WHEN
        List<Product> result = similarProductsUseCase.getSimilarProducts(query);

        // THEN
        assertThat(result)
                .hasSize(1)
                .containsExactly(product2);
    }

    @Test
    void shouldPropagateProductNotFoundException() {

        // GIVEN
        SimilarProductsQuery query = new SimilarProductsQuery("1");

        when(similarProductsOutputPort.findSimilarProductIds("1"))
                .thenThrow(new ProductNotFoundException("Product not found: 1"));

        // WHEN
        var exception = assertThrows(ProductNotFoundException.class, () -> {
            similarProductsUseCase.getSimilarProducts(query);
        });

        assertThat(exception.getMessage()).isEqualTo("Product not found: 1");

        // Verify
        verify(similarProductsOutputPort, never()).findProductById(anyString());
    }
}
