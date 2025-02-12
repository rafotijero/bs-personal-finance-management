package app.rafo.bs_personal_finance_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // 🔹 Evita enviar valores nulos en JSON de respuesta
public class TransactionDTO {

    private Long id;

    private Long bankAccountId;  // 🔹 Ahora opcional en `PUT`

    private String transactionType; // 🔹 Ahora opcional en `PUT`

    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    private LocalDateTime transactionDate;

    private String description;

    private String receiptFilePath;
}
