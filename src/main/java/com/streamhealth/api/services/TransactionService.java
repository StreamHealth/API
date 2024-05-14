package com.streamhealth.api.services;

import com.streamhealth.api.dtos.TransactionDto;
import com.streamhealth.api.dtos.UserDto;
import com.streamhealth.api.entities.Transaction;
import com.streamhealth.api.entities.User;
import com.streamhealth.api.exceptions.AppException;
import com.streamhealth.api.mappers.TransactionMapper;
import com.streamhealth.api.repositories.TransactionRepository;
import com.streamhealth.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    UserRepository userRepository;

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
        UserDto currentUser = (UserDto) authentication.getPrincipal();
        String currentLogin = currentUser.getLogin();

        Optional<User> userOptional = userRepository.findByLogin(currentLogin);
        if (userOptional.isEmpty()) {
            throw new AppException("Unknown user", HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();

        validateTransactionDto(transactionDto);

        Transaction transaction = transactionMapper.toTransaction(transactionDto);
        transaction.setTransactionDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        transaction.setCashier(user);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toTransactionDto(savedTransaction);
    }

    public void deleteTransaction(Long transactionId) {
        return;
    }

    public TransactionDto getTransaction(Long transactionId) {
        return null;
    }


}
