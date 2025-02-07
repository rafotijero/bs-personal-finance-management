package app.rafo.bs_personal_finance_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fixed_incomes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedIncome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Usuario propietario del ingreso fijo

    @Column(nullable = false)
    private BigDecimal amount;  // Monto del ingreso fijo

    @Column(nullable = false)
    private String source;  // Fuente del ingreso (ejemplo: salario, renta, etc.)

    @Column(nullable = false)
    private LocalDateTime incomeDate;  // Fecha del ingreso

}