package app.rafo.bs_personal_finance_management.repository;

import app.rafo.bs_personal_finance_management.model.VariableTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariableTransactionRepository extends JpaRepository<VariableTransaction, Long> {
    List<VariableTransaction> findByBankAccount_Id(Long bankAccountId);
}

