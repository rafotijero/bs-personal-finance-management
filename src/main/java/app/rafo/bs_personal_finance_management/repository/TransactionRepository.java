package app.rafo.bs_personal_finance_management.repository;

import app.rafo.bs_personal_finance_management.model.Transaction;
import app.rafo.bs_personal_finance_management.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Obtener todas las transacciones de una cuenta bancaria específica (no eliminadas)
    List<Transaction> findByBankAccountIdAndIsDeleted(Long bankAccountId, Character isDeleted);

    // Obtener todas las transacciones de un usuario (a través de las cuentas bancarias)
    List<Transaction> findByBankAccount_Owner_IdAndIsDeleted(Long ownerId, Character isDeleted);

    // Obtener transacciones por tipo (Ingreso o Egreso) de una cuenta específica
    List<Transaction> findByBankAccountIdAndTransactionTypeAndIsDeleted(Long bankAccountId,
                                                                        TransactionType transactionType,
                                                                        Character isDeleted);

    List<Transaction> findTopNByBankAccount_Owner_IdAndIsDeletedOrderByTransactionDateDesc(
            Long userId, char isDeleted, Pageable pageable);

    // 🔹 Listar todas las transacciones de un usuario utilizando relaciones JPA
    @Query("SELECT t FROM Transaction t " +
            "JOIN t.bankAccount ba " +
            "JOIN ba.owner u " +
            "WHERE u.id = :idUser AND t.isDeleted = '0'")
    List<Transaction> findAllByUserId(@Param("idUser") Long idUser);

    // 🔹 Listar un número dinámico de transacciones recientes del usuario utilizando relaciones JPA
    @Query("SELECT t FROM Transaction t " +
            "JOIN t.bankAccount ba " +
            "JOIN ba.owner u " +
            "WHERE u.id = :idUser AND t.isDeleted = '0' " +
            "ORDER BY t.id DESC")
    List<Transaction> findTopTransactionsByUserId(@Param("idUser") Long idUser, org.springframework.data.domain.Pageable pageable);

}
