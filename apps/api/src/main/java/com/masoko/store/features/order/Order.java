package com.masoko.store.features.order;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import org.hibernate.annotations.Changelog;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal total_amount;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @CreationTimestamp
    private LocalDateTime created_at;
}