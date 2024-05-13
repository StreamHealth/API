package com.streamhealth.api.repositories;

import com.streamhealth.api.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findByProductNameContaining(String productName);

    Page<Product> findByProductNameContaining(String productName, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
}
