package com.tcc.pedido_service.infra.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoConsumer {
    @RabbitListener(queues = RabbitMQConfig.PEDIDO_CRIADO_QUEUE)
    public void receberPedidoCriado(PedidoCriadoEvent event) {
        System.out.println("📦 Pedido recebido - ID: " + event.getPedidoId());
        System.out.println("👤 Pagamento ID: " + event.getPagamentoId());
        System.out.println("💵 Valor: " + event.getValor());
    }

    @RabbitListener(queues = RabbitMQConfig.PEDIDO_DELETADO_QUEUE)
    public void processarPedidoDeletado(Long pedidoId) {
        System.out.println("🚮 Pedido deletado - ID: " + pedidoId);
        // Lógica para atualizar o cliente-service
    }
}
