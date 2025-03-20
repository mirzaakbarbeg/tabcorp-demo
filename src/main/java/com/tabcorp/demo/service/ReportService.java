package com.tabcorp.demo.service;

import com.tabcorp.demo.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportService {

    private final TransactionRepository transactionRepository;

    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Map<Integer, Long> calculateCostPerCustomer() {
        log.info("Calculating total cost per customer");
        List<Object[]> results = transactionRepository.findTotalCostPerCustomer();
        Map<Integer, Long> report = new HashMap<>();
        for (Object[] row : results) {
            Integer customerID = (Integer) row[0];
            Long totalCost = (Long) row[1];
            report.put(customerID, totalCost);
            log.debug("Customer ID: {}, Total cost: {}", customerID, totalCost);
        }
        log.info("Calculated cost for {} customers", report.size());
        return report;
    }

    public Map<String, Long> calculateCostPerProduct() {
        log.info("Calculating total cost per product");
        List<Object[]> results = transactionRepository.findTotalCostPerProduct();
        Map<String, Long> report = new HashMap<>();
        for (Object[] row : results) {
            String productCode = (String) row[0];
            Long totalCost = (Long) row[1];
            report.put(productCode, totalCost);
            log.debug("Product code: {}, Total cost: {}", productCode, totalCost);
        }
        log.info("Calculated cost for {} products", report.size());
        return report;
    }

    public Long countTransactionsForAustralia() {
        log.info("Counting transactions for customers from Australia");
        Long count = transactionRepository.countTransactionsForAustralia();
        log.info("Found {} transactions for customers in Australia", count);
        return count;
    }
}
