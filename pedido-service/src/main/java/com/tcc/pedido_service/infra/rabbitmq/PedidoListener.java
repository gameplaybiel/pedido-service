package com.tcc.pedido_service.infra.rabbitmq;

import com.tcc.pedido_service.domain.model.Pedido;
import com.tcc.pedido_service.domain.repository.PedidoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoListener {
    private final RabbitTemplate rabbitTemplate;
    private final PedidoRepository pedidoRepository;

    public PedidoListener(RabbitTemplate rabbitTemplate, PedidoRepository pedidoRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.pedidoRepository = pedidoRepository;
    }

    @RabbitListener(queues = "${pedido.queue}")
    public void handlePedidoCriado(Pedido pedido) {
        pedidoRepository.save(pedido);
        System.out.println("Pedido processado: " + pedido.getId());

        // Enviar para pagamento
        rabbitTemplate.convertAndSend(
                "pagamento.exchange",
                "pagamento.routing.key",
                pedido
        );
    }
}
