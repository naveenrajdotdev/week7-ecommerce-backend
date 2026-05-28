package com.ecommerce.service;

import com.ecommerce.model.dto.OrderDTO;
import com.ecommerce.model.entity.*;
import com.ecommerce.model.enums.OrderStatus;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Transactional
    public OrderDTO placeOrder(Long userId, List<OrderDTO.OrderItemDTO> items) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderItem> orderItems = items.stream().map(i -> {
            Product p = productRepository.findById(i.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            productService.reduceStock(p.getId(), i.getQuantity());
            return OrderItem.builder().product(p).quantity(i.getQuantity()).unitPrice(p.getPrice()).build();
        }).collect(Collectors.toList());

        BigDecimal total = orderItems.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder().user(user).items(orderItems)
                .totalAmount(total).status(OrderStatus.PENDING).build();
        orderItems.forEach(i -> i.setOrder(order));

        return toDTO(orderRepository.save(order));
    }

    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public OrderDTO getById(Long id) {
        return orderRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public OrderDTO updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return toDTO(orderRepository.save(order));
    }

    private OrderDTO toDTO(Order o) {
        List<OrderDTO.OrderItemDTO> itemDTOs = o.getItems().stream().map(i ->
                OrderDTO.OrderItemDTO.builder()
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice()).build()
        ).collect(Collectors.toList());

        return OrderDTO.builder().id(o.getId()).userId(o.getUser().getId())
                .items(itemDTOs).status(o.getStatus())
                .totalAmount(o.getTotalAmount()).createdAt(o.getCreatedAt()).build();
    }
}