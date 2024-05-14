package com.streamhealth.api.repositories;

import com.streamhealth.api.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageable);
    @EntityGraph(attributePaths = {"products.product"})
    Optional<Transaction> findById(Long id);
}
