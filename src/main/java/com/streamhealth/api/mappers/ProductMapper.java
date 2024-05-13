package com.streamhealth.api.mappers;

import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto productDto);
    List<ProductDto> toProductDtos(List<Product> products);

    @Mapping(target = "productName", source = "productDto.productName")
    @Mapping(target = "productDescription", source = "productDto.productDescription")
    @Mapping(target = "productPrice", source = "productDto.productPrice")
    @Mapping(target = "productStock", source = "productDto.productStock")
    @Mapping(target = "productId", ignore = true)
    void updateProductFromDto(ProductDto productDto, @MappingTarget Product product);

    default Page<ProductDto> toProductDtos(Page<Product> products) {
        return products.map(this::toProductDto);
    }
}
