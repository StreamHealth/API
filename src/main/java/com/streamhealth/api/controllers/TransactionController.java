package com.streamhealth.api.controllers;


import com.streamhealth.api.dtos.TransactionDto;
import com.streamhealth.api.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get_transaction/{transactionId}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long transactionId) {
        TransactionDto transactionData = transactionService.getTransaction(transactionId);
        return ResponseEntity.ok(transactionData);
    }

    @PostMapping("/add_transaction")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionDto transactionDto) {
        transactionService.validateTransactionDto(transactionDto);
        TransactionDto transactionData = transactionService.addTransaction(transactionDto);
        return ResponseEntity.ok(transactionData);
    }

    @DeleteMapping("/delete_transaction/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }
}
