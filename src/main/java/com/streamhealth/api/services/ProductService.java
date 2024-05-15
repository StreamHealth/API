package com.streamhealth.api.services;

import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.entities.Product;
import com.streamhealth.api.exceptions.AppException;
import com.streamhealth.api.mappers.ProductMapper;
import com.streamhealth.api.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productMapper.toProductDtos(productRepository.findAll(pageable));
    }

    public Page<ProductDto> searchProductsByName(String productName, Pageable pageable) {
        return productMapper.toProductDtos(productRepository.findByProductNameContaining(productName, pageable));
    }

    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        return productMapper.toProductDto(product);
    }

    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductDto(savedProduct);
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

    public void validateProductDto(ProductDto productDto) {
        List<String> missingFields = new ArrayList<>();

        if (productDto.getProductName() == null) {
            missingFields.add("productName");
        }
        if (productDto.getProductDescription() == null) {
            missingFields.add("productDescription");
        }
        if (productDto.getProductPrice() == null) {
            missingFields.add("productPrice");
        }
        if (productDto.getProductStock() == null) {
            missingFields.add("productStock");
        }

        if (!missingFields.isEmpty()) {
            throw new AppException("Product details are incomplete. Missing fields: " + String.join(", ", missingFields), HttpStatus.BAD_REQUEST);
        }
    }


}
