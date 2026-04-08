package com.restaurante.api.orders.service;

import com.restaurante.api.common.exception.ResourceNotFoundException;
import com.restaurante.api.model.EstatusPedido;
import com.restaurante.api.model.Pedido;
import com.restaurante.api.orders.dto.OrderSummary;
import com.restaurante.api.orders.dto.UpdateOrderStatusRequest;
import com.restaurante.api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PedidoRepository pedidoRepository;

    @Transactional(readOnly = true)
    public List<OrderSummary> listOrders() {
        return pedidoRepository.findAllByOrderByFechaDesc().stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional
    public OrderSummary updateStatus(Integer orderId, UpdateOrderStatusRequest request) {
        Pedido pedido = pedidoRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        pedido.setEstatus(parseStatus(request.status()));
        return toSummary(pedidoRepository.save(pedido));
    }

    private OrderSummary toSummary(Pedido pedido) {
        return new OrderSummary(
                pedido.getId(),
                pedido.getEstatus().name(),
                pedido.getTotal(),
                pedido.getCliente() != null ? pedido.getCliente().getNombre() : null,
                pedido.getMesa() != null ? pedido.getMesa().getNumero() : null,
                pedido.getFecha()
        );
    }

    private EstatusPedido parseStatus(String rawStatus) {
        try {
            return EstatusPedido.valueOf(rawStatus.trim().toLowerCase());
        } catch (IllegalArgumentException exception) {
            throw new ResourceNotFoundException("Unknown order status");
        }
    }
}
