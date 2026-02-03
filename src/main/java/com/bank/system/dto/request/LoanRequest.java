package com.bank.system.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoanRequest {
    private String type; // Personal, Home, Car
    private BigDecimal amount;
}
