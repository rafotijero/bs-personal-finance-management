package app.rafo.bs_personal_finance_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankDTO {

    private Long id;

    @NotBlank(message = "Bank name is required")
    private String name;

    @NotBlank(message = "Initials are required")
    @Size(max = 10, message = "Initials must be at most 10 characters")
    private String initials;

    @NotBlank(message = "Country is required")
    private String country;

    private String logo;
}
