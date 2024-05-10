package com.streamhealth.api.mappers;

import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.entities.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto productDto);
    List<ProductDto> toProductDtos(List<Product> products);

}
