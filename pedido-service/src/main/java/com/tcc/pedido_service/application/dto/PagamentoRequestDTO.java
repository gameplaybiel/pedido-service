package com.tcc.pedido_service.application.dto;

import java.math.BigDecimal;

public class PagamentoRequestDTO {
    private Long pedidoId;
    private BigDecimal valor;
    private Long clienteId;

    public PagamentoRequestDTO(){}

    public PagamentoRequestDTO(Long pedidoId, BigDecimal valor, Long clienteId) {
        this.pedidoId = pedidoId;
        this.valor = valor;
        this.clienteId = clienteId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}
