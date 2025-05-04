package com.tcc.pedido_service.application.usecase;

import com.tcc.pedido_service.application.dto.PedidoDTO;
import com.tcc.pedido_service.domain.model.Pedido;
import com.tcc.pedido_service.domain.repository.PedidoRepository;
import com.tcc.pedido_service.infra.config.ClienteClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PedidoUseCase {
    private final PedidoRepository repository;
    private final ClienteClient clienteClient; // Feign Client

    public PedidoUseCase(PedidoRepository repository, ClienteClient clienteClient) {
        this.repository = repository;
        this.clienteClient = clienteClient;
    }

    public Pedido criarPedido(PedidoDTO dto) {
        // Valida se o cliente existe
        if (!clienteClient.existeCliente(dto.getClienteId())) {
            throw new IllegalArgumentException("Cliente não encontrado!");
        }

        // Validação básica do valor
        if (dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do pedido deve ser positivo");
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
