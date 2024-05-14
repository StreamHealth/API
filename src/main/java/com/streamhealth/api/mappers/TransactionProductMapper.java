package com.streamhealth.api.mappers;

import com.streamhealth.api.dtos.TransactionDto;
import com.streamhealth.api.entities.TransactionProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionProductMapper {
    @Mapping(target = "transactionProductId", source = "productId")
    @Mapping(target = "quantitySold", source = "quantitySold")
    TransactionProduct toTransactionProduct(TransactionDto.ProductSaleDto productSaleDto);

    @Mapping(target = "productId", source = "transactionProductId")
    @Mapping(target = "quantitySold", source = "quantitySold")
    TransactionDto.ProductSaleDto toProductSaleDto(TransactionProduct transactionProduct);
    List<TransactionProduct> toTransactionProducts(List<TransactionDto.ProductSaleDto> productSaleDtos);

}
