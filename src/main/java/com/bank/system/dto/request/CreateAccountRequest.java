package com.bank.system.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal initialBalance;
}
