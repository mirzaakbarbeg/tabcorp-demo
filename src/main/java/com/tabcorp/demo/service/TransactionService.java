package com.tabcorp.demo.service;

import com.tabcorp.demo.dto.TransactionDTO;
import com.tabcorp.demo.entity.Customer;
import com.tabcorp.demo.entity.Product;
import com.tabcorp.demo.entity.Transaction;
import com.tabcorp.demo.exception.CustomerNotFoundException;
import com.tabcorp.demo.exception.ProductNotFoundException;
import com.tabcorp.demo.exception.ProductNotActiveException;
import com.tabcorp.demo.exception.TransactionCostExceededException;
import com.tabcorp.demo.repository.CustomerRepository;
import com.tabcorp.demo.repository.ProductRepository;
import com.tabcorp.demo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionService {

    public static final String ACTIVE = "Active";
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              ProductRepository productRepository,
                              CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void processTransaction(TransactionDTO dto) {
        log.info("Processing transaction for product {} and customer ID {}",
                dto.getProductCode(), dto.getCustomerID());

        // Lookup product
        Product product = productRepository.findById(dto.getProductCode())
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with code: " + dto.getProductCode()));

        // Validate product is active
        if (!ACTIVE.equalsIgnoreCase(product.getStatus())) {
            throw new ProductNotActiveException("Product must be active");
        }

        // Lookup customer
        Customer customer = customerRepository.findById(dto.getCustomerID())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found with ID: " + dto.getCustomerID()));

        // Compute total cost and validate it does not exceed 5000
        Transaction transaction = getTransaction(dto, product, customer);

        transactionRepository.save(transaction);
        log.info("Transaction processed successfully");
    }

    private static Transaction getTransaction(TransactionDTO dto, Product product, Customer customer) {
        int unitCost = product.getCost();
        int quantity = dto.getQuantity();
        int totalCost = unitCost * quantity;
        if (totalCost > 5000) {
            throw new TransactionCostExceededException("Total cost of transaction must not exceed 5000");
        }

        // (The transaction time is validated by the DTO constraint @FutureOrPresent)
        Transaction transaction = new Transaction();
        transaction.setTransactionTime(dto.getTransactionTime());
        transaction.setQuantity(quantity);
        transaction.setProduct(product);
        transaction.setCustomer(customer);
        return transaction;
    }
}

