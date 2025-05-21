package com.tcc.pedido_service.infra.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${pedido.queue}")
    private String pedidoQueue;

    @Value("${pagamento.queue}")
    private String pagamentoQueue;

    @Value("${cliente.queue}")  // Adicione se for usar
    private String clienteQueue;

    @Bean
    Queue pedidoQueue() {
        return new Queue(pedidoQueue, true);  // Fila durável
    }

    @Bean
    Queue pagamentoQueue() {
        return new Queue(pagamentoQueue, true);
    }

    // Opcional: Se precisar da fila do cliente
    @Bean
    Queue clienteQueue() {
        return new Queue(clienteQueue, true);
    }
}