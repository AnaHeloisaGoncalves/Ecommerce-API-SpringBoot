package com.goncalves.ecommerce.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String address;

    private LocalDateTime createdAt;

    @OneToMany (mappedBy = "customer")
    private List<Order> orders;
}
