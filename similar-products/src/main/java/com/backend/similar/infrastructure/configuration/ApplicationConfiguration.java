package com.backend.similar.infrastructure.configuration;

import com.backend.similar.application.ports.in.SimilarProductsInputPort;
import com.backend.similar.application.ports.out.SimilarProductsOutputPort;
import com.backend.similar.application.usecases.SimilarProductsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    SimilarProductsInputPort similarProductsInputPort(SimilarProductsOutputPort similarProductsOutputPort) {
        return new SimilarProductsUseCase(similarProductsOutputPort);
    }
}
