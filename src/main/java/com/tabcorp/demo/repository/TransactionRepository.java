package com.tabcorp.demo.repository;

import com.tabcorp.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Total cost per customer
    @Query("SELECT t.customer.customerID, SUM(t.quantity * t.product.cost) " +
            "FROM Transaction t GROUP BY t.customer.customerID")
    List<Object[]> findTotalCostPerCustomer();

    // Total cost per product
    @Query("SELECT t.product.productCode, SUM(t.quantity * t.product.cost) " +
            "FROM Transaction t GROUP BY t.product.productCode")
    List<Object[]> findTotalCostPerProduct();

    // Number of transactions where the customer location is 'Australia'
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.customer.location = 'Australia'")
    Long countTransactionsForAustralia();
}
