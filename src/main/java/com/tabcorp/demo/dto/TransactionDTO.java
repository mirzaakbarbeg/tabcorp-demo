package com.tabcorp.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDTO {

    @NotNull(message = "Transaction time is required")
    @FutureOrPresent(message = "Transaction time must not be in the past")
    @JsonProperty("transaction_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime transactionTime;

    @NotNull(message = "Customer ID is required")
    @JsonProperty("customer_id")
    private Integer customerID;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "Product code is required")
    @JsonProperty("product_code")
    private String productCode;
}
