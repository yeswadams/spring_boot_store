package com.masoko.store.features.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(name= "actual_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal actualPrice;

    @Column(name= "discounted_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal discountedPrice;

    @Column(nullable = true)
    private String sku;

    @Column(name = "amount_in_stock", nullable = false)
    private Integer amountInStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductReview> reviews = new ArrayList<>();

//    Getter and setter methods
    public void addReview(ProductReview review) {
        reviews.add(review);
        review.setProduct(null);
    }

    public void removeReview(ProductReview review) {
        reviews.remove(review);
        review.setProduct(null);
    }
}
