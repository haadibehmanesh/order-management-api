package io.github.haadibehmanesh.ordermanagement;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest {

    @Test
    void shouldCreateAndStoreOrder() {
        OrderService service = new OrderService();
        CreateOrderRequest request = new CreateOrderRequest(
                "Notebook",
                2,
                new BigDecimal("12.50")
        );

        Order order = service.createOrder(request);

        assertThat(order.id()).isNotNull();
        assertThat(order.productName()).isEqualTo("Notebook");
        assertThat(order.quantity()).isEqualTo(2);
        assertThat(order.unitPrice()).isEqualByComparingTo("12.50");
        assertThat(order.status()).isEqualTo(OrderStatus.NEW);
        assertThat(service.getOrders()).containsExactly(order);
    }
}