package app.rafo.bs_personal_finance_management.repository;

import app.rafo.bs_personal_finance_management.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {

    // ✅ Buscar un banco por nombre excluyendo los eliminados
    Optional<Bank> findByNameAndIsDeleted(String name, Character isDeleted);

    // ✅ Buscar todos los bancos activos (no eliminados)
    List<Bank> findAllByIsDeleted(Character isDeleted);

    // ✅ Verificar si un banco con el mismo nombre ya existe (para evitar duplicados)
    boolean existsByNameAndIsDeleted(String name, Character isDeleted);

    boolean existsByInitialsAndIsDeleted(String initials, Character isDeleted);
}
