package io.github.haadibehmanesh.ordermanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldReturnEmptyListWhenNoOrdersExist() throws Exception {
        when(orderService.getOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldRejectOrderWithZeroQuantity() throws Exception {
        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "productName": "Notebook",
                                "quantity": 0,
                                "unitPrice": 12.50
                            }
                            """)
                )
                .andExpect(status().isBadRequest());

        org.mockito.Mockito.verifyNoInteractions(orderService);
    }

    @Test
    void shouldCreateOrderWithValidRequest() throws Exception {
        var request = new CreateOrderRequest(
                "Notebook", 2, new java.math.BigDecimal("12.50")
        );
        var id = java.util.UUID.fromString(
                "123e4567-e89b-12d3-a456-426614174000"
        );
        var order = new Order(
                id, "Notebook", 2,
                new java.math.BigDecimal("12.50"), OrderStatus.NEW
        );

        when(orderService.createOrder(request)).thenReturn(order);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "productName": "Notebook",
                                "quantity": 2,
                                "unitPrice": 12.50
                            }
                            """)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.productName").value("Notebook"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.unitPrice").value(12.50))
                .andExpect(jsonPath("$.status").value("NEW"));

        org.mockito.Mockito.verify(orderService).createOrder(request);
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        var id = java.util.UUID.fromString(
                "123e4567-e89b-12d3-a456-426614174000"
        );

        when(orderService.getOrderById(id))
                .thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/orders/{id}", id))
                .andExpect(status().isNotFound());

        org.mockito.Mockito.verify(orderService).getOrderById(id);
    }

    @Test
    void shouldReturnOrderWhenIdExists() throws Exception {
        var id = java.util.UUID.fromString(
                "123e4567-e89b-12d3-a456-426614174000"
        );
        var order = new Order(
                id, "Notebook", 2,
                new java.math.BigDecimal("12.50"), OrderStatus.NEW
        );

        when(orderService.getOrderById(id))
                .thenReturn(java.util.Optional.of(order));

        mockMvc.perform(get("/api/orders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.productName").value("Notebook"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.unitPrice").value(12.50))
                .andExpect(jsonPath("$.status").value("NEW"));

        org.mockito.Mockito.verify(orderService).getOrderById(id);
    }
}