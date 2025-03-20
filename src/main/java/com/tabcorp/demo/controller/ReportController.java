package com.tabcorp.demo.controller;

import com.tabcorp.demo.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/customer-costs")
    public ResponseEntity<Map<Integer, Long>> getTotalCostPerCustomer() {
        log.info("Received request for total cost per customer");
        Map<Integer, Long> report = reportService.calculateCostPerCustomer();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/product-costs")
    public ResponseEntity<Map<String, Long>> getTotalCostPerProduct() {
        log.info("Received request for total cost per product");
        Map<String, Long> report = reportService.calculateCostPerProduct();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/australia-transactions")
    public ResponseEntity<Long> getAustralianTransactionCount() {
        log.info("Received request for Australian transactions count");
        Long count = reportService.countTransactionsForAustralia();
        return ResponseEntity.ok(count);
    }
}
