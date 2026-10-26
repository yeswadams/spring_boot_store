package com.masoko.store.features.order;

import com.masoko.store.features.product.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product_id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_at_purchase" nullable = false)
    private BigDecimal priceAtPurchase;

}