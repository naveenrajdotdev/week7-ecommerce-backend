package com.ecommerce.service;

import com.ecommerce.exception.PaymentFailedException;
import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.Payment;
import com.ecommerce.model.enums.PaymentStatus;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public Payment processPayment(Long orderId, String method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Simulate payment processing
        boolean success = !method.equalsIgnoreCase("FAIL");
        if (!success) throw new PaymentFailedException("Payment failed for order: " + orderId);

        Payment payment = Payment.builder()
                .order(order).amount(order.getTotalAmount())
                .method(method).status(PaymentStatus.COMPLETED)
                .paidAt(LocalDateTime.now()).build();

        return paymentRepository.save(payment);
    }

    public Payment getByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
    }
}