package com.streamhealth.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "productName", nullable = false)
    private String productName;

    @Column(name = "productDescription", nullable = false)
    private String productDescription;

    @Column(name = "productPrice", nullable = false)
    private BigDecimal productPrice;

    @Column(name = "productStock", nullable = false)
    private Integer productStock;

}
