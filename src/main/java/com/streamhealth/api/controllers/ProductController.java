package com.streamhealth.api.controllers;

import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/api/v1/product/get_products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto>productsData = productService.getAllProducts();
        return ResponseEntity.ok(productsData);
    }
}
