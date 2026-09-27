package io.github.haadibehmanesh.ordermanagement;

import java.math.BigDecimal;
import java.util.UUID;

public record Order(
        UUID id,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        OrderStatus status
) {
}