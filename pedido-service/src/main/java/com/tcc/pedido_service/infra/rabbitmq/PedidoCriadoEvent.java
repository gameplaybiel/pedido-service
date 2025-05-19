package com.tcc.pedido_service.infra.rabbitmq;

import java.io.Serializable;
import java.math.BigDecimal;

public class PedidoCriadoEvent implements Serializable {
    private Long pedidoId;
    private Long pagamentoId;
    private String descricao;
    private BigDecimal valor;
    private String status; // Ex: "CRIADO", "PROCESSANDO"

    // Construtor vazio (obrigatório para desserialização)
    public PedidoCriadoEvent() {
    }

    // Construtor com campos
    public PedidoCriadoEvent(Long pedidoId, Long pagamentoId, String descricao, BigDecimal valor) {
        this.pedidoId = pedidoId;
        this.pagamentoId = pagamentoId;
        this.descricao = descricao;
        this.valor = valor;
        this.status = "CRIADO"; // Valor padrão
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(Long pagamentoId) {
        this.pagamentoId = pagamentoId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}