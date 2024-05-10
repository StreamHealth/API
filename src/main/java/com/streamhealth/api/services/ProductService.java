package com.streamhealth.api.services;

import com.streamhealth.api.dtos.ProductDto;
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
}
