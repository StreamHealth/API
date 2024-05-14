package com.streamhealth.api.repositories;

import com.streamhealth.api.entities.TransactionProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionProductRepository extends JpaRepository<TransactionProduct, Long> {
}
