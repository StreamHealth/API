package com.streamhealth.api.controllers;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.streamhealth.api.dtos.ProductDto;
import com.streamhealth.api.dtos.TransactionDto;
import com.streamhealth.api.entities.User;
import com.streamhealth.api.exceptions.AppException;
import com.streamhealth.api.repositories.UserRepository;
import com.streamhealth.api.services.TransactionService;
import com.streamhealth.api.utilities.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @GetMapping("/get_transactions")
    public ResponseEntity<Page<TransactionDto>> getAllTransactions(@RequestParam(required = false) Boolean filterByCashier,
                                                                   @RequestParam(required = false) String transactionDate,
                                                                   @RequestParam(required = false) Long transactionId,
                                                                   @PageableDefault(size = 5) Pageable pageable,
                                                                   HttpServletRequest request) {
        Page<TransactionDto> transactionsData;
        if (Boolean.TRUE.equals(filterByCashier)) {
            String login = JWTUtil.extractLoginFromToken(request);
            User cashier = userRepository.findByLogin(login)
                    .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
            Long cashierId = cashier.getId();
            transactionsData = transactionService.getAllTransactionsByCashierId(cashierId, transactionId, transactionDate, pageable);
        } else {
            transactionsData = transactionService.getAllTransactions(transactionId, transactionDate, pageable);
        }
        return ResponseEntity.ok(transactionsData);
    }

    @GetMapping("/get_transaction/{transactionId}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long transactionId) {
        TransactionDto transactionData = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transactionData);
    }

    @PostMapping("/add_transaction")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionDto transactionDto,
                                            HttpServletRequest request) {
        String login = JWTUtil.extractLoginFromToken(request);
        User cashier = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        transactionService.validateTransactionDto(transactionDto);
        TransactionDto transactionData = transactionService.addTransaction(transactionDto, cashier);
        return ResponseEntity.ok(transactionData);
    }

    @DeleteMapping("/delete_transaction/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId, HttpServletRequest request) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }
}
