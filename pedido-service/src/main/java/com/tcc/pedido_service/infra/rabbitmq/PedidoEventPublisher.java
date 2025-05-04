package com.tcc.pedido_service.infra.rabbitmq;

import com.tcc.pedido_service.domain.model.Pedido;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    // Nomes das filas/exchanges (devem bater com a configuração no RabbitMQConfig)
    private static final String EXCHANGE_NAME = "pedido.exchange";
    private static final String CRIADO_ROUTING_KEY = "pedido.criado";
    private static final String DELETADO_ROUTING_KEY = "pedido.deletado";

    public PedidoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarPedidoCriado(Pedido pedido) {
        rabbitTemplate.convertAndSend(
                EXCHANGE_NAME,
                CRIADO_ROUTING_KEY,
                pedido
        );
    }

    // Método novo para publicar eventos de deleção
    public void publicarPedidoDeletado(Long pedidoId) {
        rabbitTemplate.convertAndSend(
                EXCHANGE_NAME,
                DELETADO_ROUTING_KEY,
                pedidoId  // Pode enviar só o ID ou um DTO com mais informações
        );
    }
}
