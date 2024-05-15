package com.streamhealth.api.repositories;

import com.streamhealth.api.entities.Transaction;
import com.streamhealth.api.entities.TransactionProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionProductRepository extends JpaRepository<TransactionProduct, Long> {
    List<TransactionProduct> findAllByTransaction(Transaction transaction);
}
