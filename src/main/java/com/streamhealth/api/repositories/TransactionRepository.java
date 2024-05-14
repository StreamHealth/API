package com.streamhealth.api.repositories;

import com.streamhealth.api.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    Page<Transaction> findByCashierId(Specification<Transaction> cashierId, Long id, Pageable pageable);

    Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageable);
}
