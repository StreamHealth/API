package com.streamhealth.api.services;

import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.entities.Product;
import com.streamhealth.api.mappers.ProductMapper;
import com.streamhealth.api.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
