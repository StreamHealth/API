package com.streamhealth.api.controllers;

import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/get_products")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required = false) String query) {
        List<ProductDto> productsData;

        if(query != null) {
            productsData = productService.searchProductsByName(query);
        } else {
            productsData = productService.getAllProducts();
        }
        return ResponseEntity.ok(productsData);
    }

    @GetMapping("/product/get_product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        ProductDto productData = productService.getProductById(productId);
        return ResponseEntity.ok(productData);
    }

    @PostMapping("/product/add_product")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        productService.validateProductDto(productDto);
        ProductDto productData = productService.addProduct(productDto);
        return ResponseEntity.ok(productData);
    }
    @PutMapping("/product/update_product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        productService.validateProductDto(productDto);
        ProductDto productData = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(productData);
    }

    @DeleteMapping("/product/delete_product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully: " + productId);
    }
}
