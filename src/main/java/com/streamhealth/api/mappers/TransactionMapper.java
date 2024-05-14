package com.streamhealth.api.mappers;

import com.streamhealth.api.dtos.TransactionDto;
import com.streamhealth.api.entities.Transaction;
import com.streamhealth.api.entities.TransactionProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "transactionId", source = "transactionId")
    Transaction toTransaction(TransactionDto transactionDto);
    List<TransactionDto> toTransactionDtos(List<Transaction> transactions);
    List<TransactionProduct> toTransactionProducts(List<TransactionDto.ProductSaleDto> productSaleDtos);

    @Mapping(target = "clientName", source = "transactionDto.clientName")
    @Mapping(target = "transactionDate", source = "transactionDto.transactionDate")
    @Mapping(target = "cashier", source = "transactionDto.cashier")
    @Mapping(target = "totalAmount", source = "transactionDto.totalAmount")
    @Mapping(target = "paymentMethod", source = "transactionDto.paymentMethod")
    @Mapping(target = "discountType", source = "transactionDto.discountType")
    @Mapping(target = "discountPercentage", source = "transactionDto.discountPercentage")
    @Mapping(target = "products", source = "transactionDto.products")
    @Mapping(target = "transactionId", ignore = true)
    void updateTransactionFromDto(TransactionDto transactionDto, @MappingTarget Transaction transaction);

    default Page<TransactionDto> toTransactionDtos(Page<Transaction> transactions) {
        return transactions.map(this::toTransactionDto);
    }

    @Mapping(target = "products", source = "transaction.products")
    TransactionDto toTransactionDto(Transaction transaction);

    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    TransactionDto.ProductSaleDto toProductSaleDto(TransactionProduct transactionProduct);
}


