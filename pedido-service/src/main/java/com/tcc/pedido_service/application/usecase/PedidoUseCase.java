package com.tcc.pedido_service.application.usecase;

import com.tcc.pedido_service.application.dto.PedidoDTO;
import com.tcc.pedido_service.domain.model.Pedido;
import com.tcc.pedido_service.domain.repository.PedidoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PedidoUseCase {
    private final PedidoRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public PedidoUseCase(PedidoRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Pedido criarPedido(PedidoDTO dto) {
        // Valida se o cliente existe
        if (dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do pedido deve ser positivo");
        }

        // Verifica se cliente existe via RabbitMQ
        Boolean clienteExiste = (Boolean) rabbitTemplate.convertSendAndReceive(
                "cliente.existe.exchange",  // Nome do exchange
                "cliente.existe.routingKey", // Routing key
                dto.getClienteId()           // ID do cliente como mensagem
        );

        if (clienteExiste == null || !clienteExiste) {
            throw new IllegalArgumentException("Cliente não encontrado!");
        }

        Pedido pedido = new Pedido(
                dto.getClienteId(),
                dto.getDescricao(),
                dto.getValor()
        );

        return repository.save(pedido);
    }

    public List<Pedido> listarPorPedido() {
        repository.flush();
        return repository.findAll();
    }

    public Pedido buscarPedidoPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
    }

    public void deletarPedido(Long id) {
        repository.deleteById(id);
    }
}
