package app.rafo.bs_personal_finance_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fixed_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Usuario propietario del gasto fijo

    @Column(nullable = false)
    private BigDecimal amount;  // Monto del gasto fijo

    @Column(nullable = false)
    private String category;  // Categoría del gasto (ejemplo: alquiler, comida, etc.)

    @Column(nullable = false)
    private LocalDateTime expenseDate;  // Fecha del gasto
}
