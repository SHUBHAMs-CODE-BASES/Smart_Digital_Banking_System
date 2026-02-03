package com.bank.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BeneficiaryRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String bankName;

    @NotBlank
    private String ifscCode;
}
