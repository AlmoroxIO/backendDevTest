package com.backend.similar.infrastructure.adapters.in.rest;

import com.backend.similar.application.commands.SimilarProductsQuery;
import com.backend.similar.application.ports.in.SimilarProductsInputPort;
import com.backend.similar.domain.product.Product;
import com.backend.similar.infrastructure.adapters.in.rest.dto.ProductDTO;
import com.backend.similar.infrastructure.adapters.in.rest.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class SimilarProductsController {

    private final SimilarProductsInputPort similarProductsInputPort;
    private final ProductMapper productMapper;

    @GetMapping("/{productId}/similar")
    public ResponseEntity<List<ProductDTO>> getSimilarProducts(@PathVariable String productId) {

        log.info("Request received: Get similar products for productId: {}", productId);

        SimilarProductsQuery query = new SimilarProductsQuery(productId);

        List<Product> similarProductsDomain = similarProductsInputPort.getSimilarProducts(query);
        List<ProductDTO> similarProductsDTO = productMapper.domainProductListToProductDTO(similarProductsDomain);

        log.info("Request processed: Found {} similar products for productId: {}", similarProductsDTO.size(), productId);

        return ResponseEntity.ok(similarProductsDTO);
    }
}
