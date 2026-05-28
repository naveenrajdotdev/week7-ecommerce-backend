package com.ecommerce.controller;

import com.ecommerce.model.entity.Payment;
import com.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public Payment processPayment(@PathVariable Long orderId, @RequestParam String method) {
        return paymentService.processPayment(orderId, method);
    }

    @GetMapping("/order/{orderId}")
    public Payment getByOrder(@PathVariable Long orderId) {
        return paymentService.getByOrderId(orderId);
    }
}