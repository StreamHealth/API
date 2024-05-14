package com.streamhealth.api.dtos;


import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private UserDto cashier;
    private Long transactionId;
    private String clientName;
    private Date transactionDate;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String discountType;
    private BigDecimal discountPercentage;
    private List<ProductSaleDto> products;

    @Data
    public static class ProductSaleDto {
        private Long productId;
        private String productName;
        private int quantitySold;
    }
}
