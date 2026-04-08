package com.restaurante.api.orders.controller;

import com.restaurante.api.orders.dto.OrderSummary;
import com.restaurante.api.orders.dto.UpdateOrderStatusRequest;
import com.restaurante.api.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staff/orders")
@RequiredArgsConstructor
public class StaffOrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderSummary> listOrders() {
        return orderService.listOrders();
    }

    @PostMapping("/{orderId}/status")
    public OrderSummary updateStatus(
            @PathVariable Integer orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return orderService.updateStatus(orderId, request);
    }
}
