package app.rafo.bs_personal_finance_management.repository;

import app.rafo.bs_personal_finance_management.model.Transaction;
import app.rafo.bs_personal_finance_management.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Buscar todas las transacciones por cuenta bancaria
    List<Transaction> findByBankAccountId(Long bankAccountId);

    // Buscar transacciones por tipo (ingreso o egreso)
    List<Transaction> findByBankAccountIdAndTransactionType(Long bankAccountId, TransactionType transactionType);
}
