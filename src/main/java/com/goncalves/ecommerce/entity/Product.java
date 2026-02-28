package com.goncalves.ecommerce.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Column(unique = true)
    private String sku;

    private BigDecimal price;
    private Integer stockQuantity;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
