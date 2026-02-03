package com.bank.system.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DepositRequest {
    private String type; // FD or RD
    private BigDecimal amount;
    private Integer tenureMonths;
}
