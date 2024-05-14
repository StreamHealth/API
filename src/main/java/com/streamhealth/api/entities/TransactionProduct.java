package com.streamhealth.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "transaction_product")
public class TransactionProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionProductId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantitySold;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
