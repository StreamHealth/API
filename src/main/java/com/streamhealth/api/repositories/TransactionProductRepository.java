package com.streamhealth.api.repositories;

import com.streamhealth.api.entities.Transaction;
import com.streamhealth.api.entities.TransactionProduct;
import com.streamhealth.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionProductRepository extends JpaRepository<TransactionProduct, Long> {
    List<TransactionProduct> findAllByTransaction(Transaction transaction);
}
