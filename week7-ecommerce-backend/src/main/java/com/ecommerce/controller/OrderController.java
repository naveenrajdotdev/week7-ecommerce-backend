package com.ecommerce.controller;

import com.ecommerce.model.dto.OrderDTO;
import com.ecommerce.model.enums.OrderStatus;
import com.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{userId}")
    public OrderDTO placeOrder(@PathVariable Long userId, @RequestBody List<OrderDTO.OrderItemDTO> items) {
        return orderService.placeOrder(userId, items);
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    @GetMapping("/{id}")
    public OrderDTO getById(@PathVariable Long id) { return orderService.getById(id); }

    @PatchMapping("/{id}/status")
    public OrderDTO updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }
}