package com.backend.similar.application.ports.in;

import com.backend.similar.application.commands.SimilarProductsQuery;
import com.backend.similar.domain.product.Product;

import java.util.List;

public interface SimilarProductsInputPort {

    List<Product> getSimilarProducts(SimilarProductsQuery similarProductsQuery);

}
