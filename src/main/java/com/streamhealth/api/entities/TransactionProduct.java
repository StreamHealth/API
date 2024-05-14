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
    private Long productId;
    private int quantitySold;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
