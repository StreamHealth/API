package com.streamhealth.api.services;

import com.streamhealth.api.dtos.TransactionDto;
import com.streamhealth.api.dtos.UserDataDto;
import com.streamhealth.api.dtos.UserDto;
import com.streamhealth.api.entities.Product;
import com.streamhealth.api.entities.Transaction;
import com.streamhealth.api.entities.TransactionProduct;
import com.streamhealth.api.entities.User;
import com.streamhealth.api.exceptions.AppException;
import com.streamhealth.api.mappers.TransactionMapper;
import com.streamhealth.api.mappers.TransactionProductMapper;
import com.streamhealth.api.mappers.UserMapper;
import com.streamhealth.api.repositories.ProductRepository;
import com.streamhealth.api.repositories.TransactionProductRepository;
import com.streamhealth.api.repositories.TransactionRepository;
import com.streamhealth.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private TransactionProductRepository transactionProductRepository;
    @Autowired
    TransactionProductMapper transactionProductMapper;


    public void validateTransactionDto(TransactionDto transactionDto) {
        List<String> missingFields = new ArrayList<>();

        if (transactionDto.getClientName() == null || transactionDto.getClientName().isEmpty()) {
            missingFields.add("clientName");
        }

        if (transactionDto.getTotalAmount() == null) {
            missingFields.add("totalAmount");
        }

        if (transactionDto.getPaymentMethod() == null || transactionDto.getPaymentMethod().isEmpty()) {
            missingFields.add("paymentMethod");
        }

        if (transactionDto.getProducts() == null || transactionDto.getProducts().isEmpty()) {
            missingFields.add("products");
        }

        if(transactionDto.getDiscountType() == null) {
            missingFields.add("discountId");
        }

        if (transactionDto.getDiscountPercentage() == null) {
            missingFields.add("discountPercentage");
        }

        if (!missingFields.isEmpty()) {
            throw new AppException("Missing fields: " + String.join(", ", missingFields), HttpStatus.BAD_REQUEST);
        }
    }

    public TransactionDto addTransaction(TransactionDto transactionDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLogin = ((UserDto) authentication.getPrincipal()).getLogin();

        User user = userRepository.findByLogin(currentLogin)
                        .orElseThrow(()-> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        validateTransactionDto(transactionDto);

        Transaction transaction = transactionMapper.toTransaction(transactionDto);
        transaction.setTransactionDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        transaction.setCashier(user);

        Transaction savedTransaction = transactionRepository.save(transaction);

        for (TransactionDto.ProductSaleDto productSaleDto : transactionDto.getProducts()) {
            Product product = productRepository.findById(productSaleDto.getProductId())
                    .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

            TransactionProduct transactionProduct = transactionProductMapper.toTransactionProduct(productSaleDto);
            transactionProduct.setTransaction(savedTransaction);
            transactionProduct.setProduct(product);

            TransactionProduct savedTransactionProduct = transactionProductRepository.save(transactionProduct);
            System.out.println("\n\nSavedTransactionProduct: " + savedTransactionProduct);
        }

        return transactionMapper.toTransactionDto(savedTransaction);
    }

    public void deleteTransaction(Long transactionId) {
    }

    public TransactionDto getTransaction(Long transactionId) {
        return null;
    }


}
