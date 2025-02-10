package app.rafo.bs_personal_finance_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "banks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Nombre del banco

    @Column(nullable = false, length = 10)
    private String initials; // Iniciales del banco (Ej: "BBVA", "BCP", "SCOTIA")

    @Column(nullable = false)
    private String country; // País donde opera el banco

    @Column(nullable = true)
    private String logo; // URL del logo

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // Fecha de creación

    @UpdateTimestamp
    private LocalDateTime updatedAt; // Fecha de última actualización

    @Column(nullable = true)
    private String createdBy; // Usuario que creó el registro

    @Column(nullable = true)
    private String updatedBy; // Último usuario que actualizó el registro

    @Column(nullable = false)
    private Character isDeleted = '0'; // '0' = No eliminado, '1' = Eliminado

    @Column(nullable = true)
    private LocalDateTime deletedAt; // Fecha de eliminación

    @Column(nullable = true)
    private String deletedBy; // Usuario que eliminó el registro
}