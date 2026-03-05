package com.goncalves.ecommerce.service;

import com.goncalves.ecommerce.dto.OrderItemRequestDTO;
import com.goncalves.ecommerce.entity.*;
import com.goncalves.ecommerce.repository.*;
import com.goncalves.ecommerce.dto.OrderRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder  (OrderRequestDTO request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Order must contain at least one item"
            );
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(Order.OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : request.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Quantity must be greater than zero"
                );
            }

            if (!product.isActive()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Product is inactive and cannot be purchased"
                );
            }

            if (itemDTO.getQuantity() > product.getStockQuantity()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Insufficient stock for product: " + product.getName()
                );
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemDTO.getQuantity()));

            product.setStockQuantity(
                    product.getStockQuantity() - itemDTO.getQuantity()
            );

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);

            totalAmount = totalAmount.add(subtotal);

        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    public Order cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getStatus() == Order.OrderStatus.CANCELED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Order is already canceled"
            );
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(
                    product.getStockQuantity() + item.getQuantity()
            );
        }

        order.setStatus(Order.OrderStatus.CANCELED);

        return orderRepository.save(order);
    }
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }
}