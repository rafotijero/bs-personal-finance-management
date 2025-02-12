package app.rafo.bs_personal_finance_management.repository;

import app.rafo.bs_personal_finance_management.model.Transaction;
import app.rafo.bs_personal_finance_management.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
