package com.goncalves.ecommerce;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String address;

    private LocalDateTime createdAt;
}
