package com.tabcorp.demo.service;

import com.tabcorp.demo.dto.TransactionDTO;
import com.tabcorp.demo.entity.Customer;
import com.tabcorp.demo.entity.Product;
import com.tabcorp.demo.entity.Transaction;
import com.tabcorp.demo.exception.CustomerNotFoundException;
import com.tabcorp.demo.exception.ProductNotActiveException;
import com.tabcorp.demo.exception.ProductNotFoundException;
import com.tabcorp.demo.exception.TransactionCostExceededException;
import com.tabcorp.demo.repository.CustomerRepository;
import com.tabcorp.demo.repository.ProductRepository;
import com.tabcorp.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testProcessTransaction_Success() {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionTime(LocalDateTime.now().plusDays(1));
        dto.setQuantity(2);
        dto.setProductCode("PRODUCT_001");
        dto.setCustomerID(10001);

        Product product = new Product();
        product.setProductCode("PRODUCT_001");
        product.setCost(50);
        product.setStatus("Active");

        Customer customer = new Customer();
        customer.setCustomerID(10001);
        customer.setFirstName("Tony");
        customer.setLastName("Stark");
        customer.setEmail("tony.stark@gmail.com");
        customer.setLocation("Australia");

        when(productRepository.findById("PRODUCT_001")).thenReturn(Optional.of(product));
        when(customerRepository.findById(10001)).thenReturn(Optional.of(customer));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        assertDoesNotThrow(() -> transactionService.processTransaction(dto));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testProcessTransaction_ProductNotFound() {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionTime(LocalDateTime.now().plusDays(1));
        dto.setQuantity(2);
        dto.setProductCode("INVALID_PRODUCT");
        dto.setCustomerID(10001);

        when(productRepository.findById("INVALID_PRODUCT")).thenReturn(Optional.empty());
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> transactionService.processTransaction(dto));
        assertTrue(exception.getMessage().contains("Product not found"));
    }

    @Test
    public void testProcessTransaction_CustomerNotFound() {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionTime(LocalDateTime.now().plusDays(1));
        dto.setQuantity(2);
        dto.setProductCode("PRODUCT_001");
        dto.setCustomerID(99999);

        Product product = new Product();
        product.setProductCode("PRODUCT_001");
        product.setCost(50);
        product.setStatus("Active");

        when(productRepository.findById("PRODUCT_001")).thenReturn(Optional.of(product));
        when(customerRepository.findById(99999)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
                () -> transactionService.processTransaction(dto));
        assertTrue(exception.getMessage().contains("Customer not found"));
    }

    @Test
    public void testProcessTransaction_ProductNotActive() {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionTime(LocalDateTime.now().plusDays(1));
        dto.setQuantity(2);
        dto.setProductCode("PRODUCT_001");
        dto.setCustomerID(10001);

        Product product = new Product();
        product.setProductCode("PRODUCT_001");
        product.setCost(50);
        product.setStatus("Inactive");

        when(productRepository.findById("PRODUCT_001")).thenReturn(Optional.of(product));
        ProductNotActiveException exception = assertThrows(ProductNotActiveException.class,
                () -> transactionService.processTransaction(dto));
        assertTrue(exception.getMessage().contains("Product must be active"));
    }

    @Test
    public void testProcessTransaction_TransactionCostExceeded() {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionTime(LocalDateTime.now().plusDays(1));
        dto.setQuantity(5);
        dto.setProductCode("PRODUCT_001");
        dto.setCustomerID(10001);

        Product product = new Product();
        product.setProductCode("PRODUCT_001");
        product.setCost(1200); // total cost = 5 * 1200 = 6000 exceeds 5000
        product.setStatus("Active");

        when(productRepository.findById("PRODUCT_001")).thenReturn(Optional.of(product));
        when(customerRepository.findById(10001)).thenReturn(Optional.of(new Customer()));

        TransactionCostExceededException exception = assertThrows(TransactionCostExceededException.class,
                () -> transactionService.processTransaction(dto));
        assertTrue(exception.getMessage().contains("Total cost of transaction must not exceed 5000"));
    }
}
