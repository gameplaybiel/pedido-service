package com.tcc.pedido_service.application.dto;

import java.math.BigDecimal;

public class PedidoDTO {
    private Long clienteId;
    private String descricao;
    private BigDecimal valor;

    public PedidoDTO(){}

    public PedidoDTO(Long clienteId, String descricao, BigDecimal valor) {
        this.clienteId = clienteId;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
