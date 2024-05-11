package com.streamhealth.api.controllers;

import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/v1/product/get_product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        ProductDto productData = productService.getProductById(productId);
        return ResponseEntity.ok(productData);
    }

    @PostMapping("/api/v1/product/add_product")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        if (productDto.getProductName() == null || productDto.getProductDescription() == null ||
                productDto.getProductPrice() == null || productDto.getProductStock() == null) {
            return new ResponseEntity<>("Product details are incomplete", HttpStatus.BAD_REQUEST);
        }
        ProductDto productData = productService.addProduct(productDto);
        return ResponseEntity.ok(productData);
    }
    @PutMapping("/api/v1/product/update_product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        if (productDto.getProductName() == null || productDto.getProductDescription() == null ||
                productDto.getProductPrice() == null || productDto.getProductStock() == null) {
            return new ResponseEntity<>("Product details are incomplete", HttpStatus.BAD_REQUEST);
        }
        ProductDto productData = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(productData);
    }

    @DeleteMapping("/api/v1/product/delete_product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully: " + productId);
    }
}
