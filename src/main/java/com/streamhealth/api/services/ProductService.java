package com.streamhealth.api.services;

import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.entities.Product;
import com.streamhealth.api.exceptions.AppException;
import com.streamhealth.api.mappers.ProductMapper;
import com.streamhealth.api.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getAllProducts() {
        return productMapper.toProductDtos(productRepository.findAll());
    }

    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductDto(savedProduct);
    }

    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        return productMapper.toProductDto(product);
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        productMapper.updateProductFromDto(productDto, product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductDto(updatedProduct);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        productRepository.deleteById(product.getProductId());
    }
}
