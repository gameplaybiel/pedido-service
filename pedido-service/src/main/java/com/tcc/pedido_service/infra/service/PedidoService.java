package com.tcc.pedido_service.infra.service;

import com.tcc.pedido_service.domain.model.Pedido;
import com.tcc.pedido_service.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public void salvarPedido(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedido(){
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPedidoPorId(Long id){
        return pedidoRepository.findById(id);
    }

    public void deletarPedido(Long id){
        pedidoRepository.deleteById(id);
    }
}
