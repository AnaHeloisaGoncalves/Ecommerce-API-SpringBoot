package com.goncalves.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class OrderRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Order must have at least one item")
    @Size(min = 1, message = "Order must have at least one item")
    private List<OrderItemRequestDTO> items;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDTO> items) {
        this.items = items;
    }
}
