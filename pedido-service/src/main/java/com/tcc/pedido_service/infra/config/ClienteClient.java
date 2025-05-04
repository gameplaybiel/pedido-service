package com.tcc.pedido_service.infra.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "cliente-service",
        url = "${cliente.service.url:http://localhost:8083}", // Valor padrão após ":"
        configuration = FeignConfig.class
)
public interface ClienteClient {
    @GetMapping("/cliente/{id}/existe")
    Boolean existeCliente(@PathVariable Long id);
}
