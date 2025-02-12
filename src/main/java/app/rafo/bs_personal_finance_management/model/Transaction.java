package app.rafo.bs_personal_finance_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount; // Cuenta bancaria involucrada en la transacción

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType; // Tipo de transacción (INGRESO, EGRESO, etc.)

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount; // Monto de la transacción

    @Column(nullable = false)
    private LocalDateTime transactionDate; // Fecha de la transacción

    @Column(length = 255)
    private String description; // Descripción opcional de la transacción

    // Archivo adjunto (puede ser imagen o PDF como constancia)
    @Column(nullable = true)
    private String receiptFilePath; // Ruta del archivo adjunto

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
