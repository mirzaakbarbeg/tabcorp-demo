package com.tabcorp.demo.controller;

import com.tabcorp.demo.dto.TransactionDTO;
import com.tabcorp.demo.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> submitTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        log.info("Received transaction request: {}", transactionDTO);
        transactionService.processTransaction(transactionDTO);
        return ResponseEntity.ok("Transaction processed successfully");
    }
}
