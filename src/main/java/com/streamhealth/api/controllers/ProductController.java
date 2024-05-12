package com.streamhealth.api.controllers;

import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get_products")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required = false, value = "search") String search) {
        List<ProductDto> productsData;
        if(search != null) {
            productsData = productService.searchProductsByName(search);
        } else {
            productsData = productService.getAllProducts();
        }
        return ResponseEntity.ok(productsData);
    }

    @GetMapping("/get_product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        ProductDto productData = productService.getProductById(productId);
        return ResponseEntity.ok(productData);
    }

    @PostMapping("/add_product")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        productService.validateProductDto(productDto);
        ProductDto productData = productService.addProduct(productDto);
        return ResponseEntity.ok(productData);
    }
    @PutMapping("/update_product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        productService.validateProductDto(productDto);
        ProductDto productData = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(productData);
    }

    @DeleteMapping("/delete_product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully: " + productId);
    }
}
