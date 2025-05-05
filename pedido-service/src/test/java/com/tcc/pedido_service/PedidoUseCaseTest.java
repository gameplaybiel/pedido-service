package com.tcc.pedido_service;

import com.tcc.pedido_service.application.dto.PedidoDTO;
import com.tcc.pedido_service.application.usecase.PedidoUseCase;
import com.tcc.pedido_service.domain.model.Pedido;
import com.tcc.pedido_service.domain.repository.PedidoRepository;
import com.tcc.pedido_service.infra.config.ClienteClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoUseCaseTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private ClienteClient clienteClient;

    @InjectMocks
    private PedidoUseCase pedidoUseCase;

    @Test
    void criarPedido_ComDadosValidos_DeveRetornarPedidoSalvo() {
        // Arrange
        PedidoDTO dto = new PedidoDTO(1L, "Notebook", new BigDecimal("2500.00"));
        Pedido pedidoSalvo = new Pedido(dto.getClienteId(), dto.getDescricao(), dto.getValor());

        when(clienteClient.existeCliente(anyLong())).thenReturn(true);
        when(repository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

        // Act
        Pedido resultado = pedidoUseCase.criarPedido(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(null, resultado.getId());
        assertEquals("Notebook", resultado.getDescricao());
        assertEquals(new BigDecimal("2500.00"), resultado.getValor());
        verify(clienteClient, times(1)).existeCliente(1L);
        verify(repository, times(1)).save(any(Pedido.class));
    }

    @Test
    void criarPedido_ComClienteInexistente_DeveLancarExcecao() {
        // Arrange
        PedidoDTO dto = new PedidoDTO(99L, "Notebook", new BigDecimal("2500.00"));

        when(clienteClient.existeCliente(anyLong())).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pedidoUseCase.criarPedido(dto));

        assertEquals("Cliente não encontrado!", exception.getMessage());
        verify(clienteClient, times(1)).existeCliente(99L);
        verify(repository, never()).save(any(Pedido.class));
    }

    @Test
    void criarPedido_ComValorZero_DeveLancarExcecao() {
        // Arrange
        PedidoDTO dto = new PedidoDTO(1L, "Notebook", BigDecimal.ZERO);

        when(clienteClient.existeCliente(anyLong())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pedidoUseCase.criarPedido(dto));

        assertEquals("Valor do pedido deve ser positivo", exception.getMessage());
        verify(clienteClient, times(1)).existeCliente(1L);
        verify(repository, never()).save(any(Pedido.class));
    }

    @Test
    void criarPedido_ComValorNegativo_DeveLancarExcecao() {
        // Arrange
        PedidoDTO dto = new PedidoDTO(1L, "Notebook", new BigDecimal("-100.00"));

        when(clienteClient.existeCliente(anyLong())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> pedidoUseCase.criarPedido(dto));

        assertEquals("Valor do pedido deve ser positivo", exception.getMessage());
        verify(repository, never()).save(any(Pedido.class));
    }

    @Test
    void listarPorPedido_QuandoExistemPedidos_DeveRetornarLista() {
        // Arrange
        Pedido pedido = new Pedido(1L, "Notebook", new BigDecimal("2500.00"));
        when(repository.findAll()).thenReturn(Collections.singletonList(pedido));

        // Act
        List<Pedido> resultado = pedidoUseCase.listarPorPedido();

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Notebook", resultado.get(0).getDescricao());
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).flush();
    }

    @Test
    void listarPorPedido_QuandoNaoExistemPedidos_DeveRetornarListaVazia() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Pedido> resultado = pedidoUseCase.listarPorPedido();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).flush();
    }

    @Test
    void buscarPedidoPorId_QuandoPedidoExiste_DeveRetornarPedido() {
        // Arrange
        Long id = null;
        Pedido pedido = new Pedido(id, "Notebook", new BigDecimal("2500.00"));
        when(repository.findById(id)).thenReturn(Optional.of(pedido));

        // Act
        Pedido resultado = pedidoUseCase.buscarPedidoPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void buscarPedidoPorId_QuandoPedidoNaoExiste_DeveLancarExcecao() {
        // Arrange
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoUseCase.buscarPedidoPorId(id));

        assertEquals("Pedido não encontrado com id: 99", exception.getMessage());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void deletarPedido_DeveChamarRepositorio() {
        // Arrange
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        // Act
        pedidoUseCase.deletarPedido(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }
}