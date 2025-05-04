package com.tcc.pedido_service.infra.controller;

import com.tcc.pedido_service.application.dto.PedidoDTO;
import com.tcc.pedido_service.application.usecase.PedidoUseCase;
import com.tcc.pedido_service.domain.model.Pedido;
import com.tcc.pedido_service.infra.rabbitmq.PedidoEventPublisher;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    private final PedidoUseCase pedidoUseCase;
    private final PedidoEventPublisher eventPublisher;

    public PedidoController(PedidoUseCase pedidoUseCase, PedidoEventPublisher eventPublisher) {
        this.pedidoUseCase = pedidoUseCase;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        try {
            Pedido pedido = pedidoUseCase.criarPedido(pedidoDTO);
            eventPublisher.publicarPedidoCriado(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoUseCase.listarPorPedido());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pedidoUseCase.buscarPedidoPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        try {
            pedidoUseCase.deletarPedido(id);
            eventPublisher.publicarPedidoDeletado(id);  // Publica evento de deleção
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
