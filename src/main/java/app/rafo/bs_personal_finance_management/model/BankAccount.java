package app.rafo.bs_personal_finance_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_accounts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber; // Número de cuenta único

    @Column(nullable = false)
    private Double balance; // Saldo de la cuenta

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType; // Tipo de cuenta (AHORRO, CORRIENTE, etc.)

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank; // Banco al que pertenece la cuenta

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner; // Usuario propietario de la cuenta
}

