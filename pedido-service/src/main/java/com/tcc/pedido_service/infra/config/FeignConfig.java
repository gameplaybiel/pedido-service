package com.tcc.pedido_service.infra.config;

import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 3); // Retry em falhas
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            return new RuntimeException("Falha na chamada ao cliente-service: " + response.status());
        };
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC; // Log reduzido
    }
}
