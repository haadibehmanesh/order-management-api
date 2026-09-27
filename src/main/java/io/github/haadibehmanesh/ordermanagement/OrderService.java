package io.github.haadibehmanesh.ordermanagement;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class OrderService {

    private final ConcurrentMap<UUID, Order> orders = new ConcurrentHashMap<>();

    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order(
                UUID.randomUUID(),
                request.productName(),
                request.quantity(),
                request.unitPrice(),
                OrderStatus.NEW
        );

        orders.put(order.id(), order);
        return order;
    }

    public List<Order> getOrders() {
        return List.copyOf(orders.values());
    }

    public java.util.Optional<Order> getOrderById(UUID id) {
        return java.util.Optional.ofNullable(orders.get(id));
    }
}