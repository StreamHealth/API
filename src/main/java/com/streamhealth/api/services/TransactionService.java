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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public TransactionDto addTransaction(TransactionDto transactionDto, User cashier) {

        Transaction transaction = transactionMapper.toTransaction(transactionDto);
        transaction.setTransactionDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        transaction.setCashier(cashier);
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

    @Transactional
    public void deleteTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException("Transaction not found", HttpStatus.NOT_FOUND));
        transactionRepository.delete(transaction);
    }

    public TransactionDto getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException("Transaction not found", HttpStatus.NOT_FOUND));
        return transactionMapper.toTransactionDto(transaction);
    }

//    public Page<TransactionDto> getAllTransactions(Long transactionId, String transactionDate, Pageable pageable) {
//        Specification<Transaction> spec = Specification.where(null);
//        if (transactionId != null) {
//            spec = spec.and((root, query, cb) -> cb.equal(root.get("transactionId"), transactionId));
//        }
//        LocalDate date;
//        if (transactionDate != null && !transactionDate.isEmpty()) {
//            date = LocalDate.parse(transactionDate);
//        } else {
//            date = LocalDate.now();
//        }
//        spec = spec.and((root, query, cb) -> cb.equal(cb.function("DATE", LocalDate.class, root.get("transactionDate")), date));
//        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);
//        return transactions.map(transactionMapper::toTransactionDto);
//    }
//
//    public Page<TransactionDto> getAllTransactionsByCashierId(Long cashierId, Long transactionId, String transactionDate, Pageable pageable) {
//        Specification<Transaction> spec = Specification.where((root, query, cb) -> cb.equal(root.get("cashier").get("id"), cashierId));
//        if (transactionId != null) {
//            spec = spec.and((root, query, cb) -> cb.equal(root.get("transactionId"), transactionId));
//        }
//        LocalDate date;
//        if (transactionDate != null && !transactionDate.isEmpty()) {
//            date = LocalDate.parse(transactionDate);
//        } else {
//            date = LocalDate.now();
//        }
//        spec = spec.and((root, query, cb) -> cb.equal(cb.function("DATE", LocalDate.class, root.get("transactionDate")), date));
//        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);
//        return transactions.map(transactionMapper::toTransactionDto);
//    }

    public Page<TransactionDto> getAllTransactions(Long transactionId, String transactionDate, Pageable pageable) {
        Specification<Transaction> spec = Specification.where(null);
        if (transactionId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("transactionId"), transactionId));
        }
        LocalDateTime startOfDay;
        LocalDateTime endOfDay;
        if (transactionDate != null && !transactionDate.isEmpty()) {
            LocalDate date = LocalDate.parse(transactionDate);
            startOfDay = date.atStartOfDay();
            endOfDay = date.plusDays(1).atStartOfDay();
        } else {
            startOfDay = LocalDate.now().atStartOfDay();
            endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        }
        spec = spec.and((root, query, cb) -> cb.between(root.get("transactionDate"), startOfDay, endOfDay));
        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);
        return transactions.map(transactionMapper::toTransactionDto);
    }

    public Page<TransactionDto> getAllTransactionsByCashierId(Long cashierId, Long transactionId, String transactionDate, Pageable pageable) {
        Specification<Transaction> spec = Specification.where((root, query, cb) -> cb.equal(root.get("cashier").get("id"), cashierId));
        if (transactionId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("transactionId"), transactionId));
        }
        LocalDateTime startOfDay;
        LocalDateTime endOfDay;
        if (transactionDate != null && !transactionDate.isEmpty()) {
            LocalDate date = LocalDate.parse(transactionDate);
            startOfDay = date.atStartOfDay();
            endOfDay = date.plusDays(1).atStartOfDay();
        } else {
            startOfDay = LocalDate.now().atStartOfDay();
            endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        }
        spec = spec.and((root, query, cb) -> cb.between(root.get("transactionDate"), startOfDay, endOfDay));
        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);
        return transactions.map(transactionMapper::toTransactionDto);
    }
}
