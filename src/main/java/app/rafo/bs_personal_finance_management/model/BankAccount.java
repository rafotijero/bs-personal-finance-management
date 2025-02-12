package app.rafo.bs_personal_finance_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private String accountDescription; // Número de cuenta único

    @Column(nullable = false, unique = true)
    private String accountNumber; // Número de cuenta único

    @Column(nullable = false)
    private BigDecimal balance; // Saldo de la cuenta

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType; // Tipo de cuenta (AHORRO, CORRIENTE, etc.)

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank; // Banco al que pertenece la cuenta

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner; // Usuario propietario de la cuenta

    // Auditoría
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // Fecha de creación

    @UpdateTimestamp
    private LocalDateTime updatedAt; // Fecha de última actualización

    @Column(nullable = true)
    private String createdBy; // Usuario que creó el registro

    @Column(nullable = true)
    private String updatedBy; // Último usuario que actualizó el registro

    // Eliminación lógica
    @Column(nullable = false)
    private Character isDeleted = '0'; // '0' = No eliminado, '1' = Eliminado

    @Column(nullable = true)
    private LocalDateTime deletedAt; // Fecha de eliminación

    @Column(nullable = true)
    private String deletedBy; // Usuario que eliminó el registro
}
