package com.streamhealth.api.services;

import com.streamhealth.api.dtos.TransactionDto;
import com.streamhealth.api.entities.Product;
import com.streamhealth.api.entities.Transaction;
import com.streamhealth.api.entities.TransactionProduct;
import com.streamhealth.api.entities.User;
import com.streamhealth.api.exceptions.AppException;
import com.streamhealth.api.mappers.TransactionMapper;
import com.streamhealth.api.mappers.TransactionProductMapper;
import com.streamhealth.api.repositories.ProductRepository;
import com.streamhealth.api.repositories.TransactionProductRepository;
import com.streamhealth.api.repositories.TransactionRepository;
import com.streamhealth.api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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
    ProductRepository productRepository;
    @Autowired
    private TransactionProductRepository transactionProductRepository;
    @Autowired
    TransactionProductMapper transactionProductMapper;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
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
        transaction.setProducts(new ArrayList<>());
        Transaction savedTransaction = transactionRepository.save(transaction);

        for (TransactionDto.ProductSaleDto productSaleDto : transactionDto.getProducts()) {
            Product product = productRepository.findById(productSaleDto.getProductId())
                    .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

            int updatedStock = product.getProductStock() - productSaleDto.getQuantitySold();
            if (updatedStock < 0) {
                throw new AppException("Insufficient stock for product: " + product.getProductName(), HttpStatus.BAD_REQUEST);
            }
            product.setProductStock(updatedStock);
            productRepository.save(product);

            TransactionProduct transactionProduct = new TransactionProduct();
            transactionProduct.setTransaction(savedTransaction);
            transactionProduct.setProduct(product);
            transactionProduct.setQuantitySold(productSaleDto.getQuantitySold());

            TransactionProduct savedTransactionProduct = transactionProductRepository.save(transactionProduct);
            savedTransaction.getProducts().add(savedTransactionProduct);
        }

        Transaction updatedTransaction = transactionRepository.save(savedTransaction);

        transactionRepository.flush();

        Transaction latestTransaction = transactionRepository.findById(updatedTransaction.getTransactionId())
                .orElseThrow(() -> new AppException("Transaction not found", HttpStatus.NOT_FOUND));

        return transactionMapper.toTransactionDto(latestTransaction);
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

    public Page<TransactionDto> getAllTransactions(Long transactionId, String transactionDate, Pageable pageable) {
        Specification<Transaction> spec = Specification.where(null);
        spec = spec.and(getDateRangeAndIdSpec(transactionId, transactionDate));
        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);
        return transactions.map(transactionMapper::toTransactionDto);
    }

    public Page<TransactionDto> getAllTransactionsByCashierId(Long cashierId, Long transactionId, String transactionDate, Pageable pageable) {
        Specification<Transaction> spec = Specification.where((root, query, cb) -> cb.equal(root.get("cashier").get("id"), cashierId));
        spec = spec.and(getDateRangeAndIdSpec(transactionId, transactionDate));
        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);
        return transactions.map(transactionMapper::toTransactionDto);
    }

    private Specification<Transaction> getDateRangeAndIdSpec(Long transactionId, String transactionDate) {
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
        Specification<Transaction> dateSpec = (root, query, cb) -> cb.between(root.get("transactionDate"), startOfDay, endOfDay);
        if (transactionId != null) {
            Specification<Transaction> idSpec = (root, query, cb) -> cb.equal(root.get("transactionId"), transactionId);
            return dateSpec.and(idSpec);
        } else {
            return dateSpec;
        }
    }
}
