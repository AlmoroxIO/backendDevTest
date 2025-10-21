package com.backend.similar.infrastructure.adapters.in.rest;

import com.backend.similar.application.commands.SimilarProductsQuery;
import com.backend.similar.application.ports.in.SimilarProductsInputPort;
import com.backend.similar.domain.Price;
import com.backend.similar.domain.product.Product;
import com.backend.similar.domain.product.ProductId;
import com.backend.similar.domain.product.ProductName;
import com.backend.similar.infrastructure.adapters.in.rest.dto.ProductDTO;
import com.backend.similar.infrastructure.adapters.in.rest.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimilarProductsControllerTest {

    @Mock
    private SimilarProductsInputPort similarProductsInputPort;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private SimilarProductsController similarProductsController;

    @Test
    void shouldDelegateToUseCaseAndMapResults() {

        // GIVEN
        String productId = "1";

        // Data
        Product productDomain = Product.rehydrate(new ProductId("2"), new ProductName("Shirt"), new Price(BigDecimal.TEN), true);
        List<Product> domainList = List.of(productDomain);

        ProductDTO productDTO = new ProductDTO("2", "Shirt", BigDecimal.TEN, true);
        List<ProductDTO> dtoList = List.of(productDTO);

        when(similarProductsInputPort.getSimilarProducts(any(SimilarProductsQuery.class)))
                .thenReturn(domainList);

        when(productMapper.domainProductListToProductDTO(domainList))
                .thenReturn(dtoList);

        // WHEN
        ResponseEntity<List<ProductDTO>> response = similarProductsController.getSimilarProducts(productId);

        // THEN
        ArgumentCaptor<SimilarProductsQuery> queryCaptor = ArgumentCaptor.forClass(SimilarProductsQuery.class);

        //Verify
        verify(similarProductsInputPort).getSimilarProducts(queryCaptor.capture());

        assertThat(queryCaptor.getValue().productId()).isEqualTo("1");

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isSameAs(dtoList);
    }
}
