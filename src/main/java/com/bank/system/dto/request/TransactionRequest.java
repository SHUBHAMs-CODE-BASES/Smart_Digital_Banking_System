package com.bank.system.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionRequest {

    private String sourceAccountNumber;

    private String targetAccountNumber; // Required for TRANSFER

    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal amount;

    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER

    private String description;
}
