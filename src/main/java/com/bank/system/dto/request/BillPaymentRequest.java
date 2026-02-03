package com.bank.system.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BillPaymentRequest {
    private Long billerId;
    private Long accountId;
    private String referenceNumber;
    private BigDecimal amount;
}
