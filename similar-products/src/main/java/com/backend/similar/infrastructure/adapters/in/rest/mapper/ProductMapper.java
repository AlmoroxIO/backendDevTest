package com.backend.similar.infrastructure.adapters.in.rest.mapper;

import com.backend.similar.domain.Price;
import com.backend.similar.domain.product.Product;
import com.backend.similar.domain.product.ProductId;
import com.backend.similar.domain.product.ProductName;
import com.backend.similar.infrastructure.adapters.in.rest.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    List<ProductDTO> domainProductListToProductDTO(List<Product> products);

    @Mapping(source = "productId", target = "id", qualifiedByName = "productIdToString")
    @Mapping(source = "productName", target = "name", qualifiedByName = "productNameToString")
    @Mapping(source = "price", target = "price", qualifiedByName = "priceToBigDecimal")
    ProductDTO domainProductToDTO(Product product);

    @Named("productIdToString")
    default String productIdToString(ProductId productId) {
        return productId.id();
    }

    @Named("productNameToString")
    default String productNameToString(ProductName productName) {
        return productName.name();
    }

    @Named("priceToBigDecimal")
    default BigDecimal priceToBigDecimal(Price price) {
        return price.amount();
    }
}
