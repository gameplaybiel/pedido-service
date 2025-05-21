package com.tcc.pedido_service.infra.controller;

import com.tcc.pedido_service.application.dto.PagamentoRequestDTO;
import com.tcc.pedido_service.application.dto.PedidoDTO;
import com.tcc.pedido_service.application.usecase.PedidoUseCase;
import com.tcc.pedido_service.domain.model.Pedido;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    private final PedidoUseCase pedidoUseCase;
    private final RestTemplate restTemplate;
    private final String PAGAMENTO_SERVICE_URL = "http://localhost:8085/pagamento";

    public PedidoController(PedidoUseCase pedidoUseCase, RestTemplate restTemplate) {
        this.pedidoUseCase = pedidoUseCase;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        try {
            Pedido pedido = pedidoUseCase.criarPedido(pedidoDTO);

            // Prepara a requisição para o serviço de pagamento
            PagamentoRequestDTO pagamentoRequest = new PagamentoRequestDTO(
                    pedido.getId(),
                    pedido.getValor(),
                    pedido.getClienteId()
            );

            // Configura headers para JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PagamentoRequestDTO> requestEntity =
                    new HttpEntity<>(pagamentoRequest, headers);

            // Faz a chamada REST
            ResponseEntity<String> response = restTemplate.exchange(
                    PAGAMENTO_SERVICE_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Se o pagamento foi criado com sucesso (HTTP 2xx)
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
            } else {
                // Se falhar, cancela o pedido (rollback)
                pedidoUseCase.deletarPedido(pedido.getId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Falha ao processar pagamento: " + response.getBody());
            }
        } catch (Exception e) {
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
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
