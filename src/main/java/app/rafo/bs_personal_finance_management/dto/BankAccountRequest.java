package app.rafo.bs_personal_finance_management.dto;

import lombok.Data;

@Data
public class BankAccountRequest {
    private Long bankId;
    private Long userId;
    private String accountNumber;
    private String accountType;
    private Double balance;
}
