package com.tcc.pedido_service.infra.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${pedido.queue.name}")
    private String pedidoQueueName;

    @Value("${pagamento.queue.name}")
    private String pagamentoQueueName;

    @Bean
    Queue pedidoQueue() {
        return new Queue(pedidoQueueName, true);
    }

    @Bean
    Queue pagamentoQueue() {
        return new Queue(pagamentoQueueName, true);
    }
}