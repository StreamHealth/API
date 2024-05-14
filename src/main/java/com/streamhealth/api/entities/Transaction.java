package com.streamhealth.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private String clientName;
    private Date transactionDate;
    private String discountType;
    private Double discountPercentage;

    @ManyToOne
    @JoinColumn(name = "cashier_id")
    private User cashier;
    private BigDecimal totalAmount;
    private String paymentMethod;

    @OneToMany(mappedBy = "transaction")
    private Set<TransactionProduct> products = new HashSet<>();
}
