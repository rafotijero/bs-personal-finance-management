package app.rafo.bs_personal_finance_management.dto;

import app.rafo.bs_personal_finance_management.model.Bank;
import app.rafo.bs_personal_finance_management.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountDTO {

    private Long id;

    @NotBlank(message = "Account number is required")
    @Size(max = 20, message = "Account number must be at most 20 characters")
    private String accountNumber;

    @NotNull(message = "Balance is required")
    @Positive(message = "Balance must be a positive number")
    private Double balance;

    @NotBlank(message = "Account type is required")
    private String accountType; // Se usará como String, pero debe coincidir con los valores de AccountType

    @NotNull(message = "Bank ID is required")
    private Long bankId;  // 🔹 Lo mantenemos para crear cuentas

    private Bank bank;  // ✅ Ahora incluirá el objeto `bank`

    @NotNull(message = "User ID is required")
    private Long ownerId;

    private User owner;  // ✅ Ahora incluye el objeto `owner`
}
