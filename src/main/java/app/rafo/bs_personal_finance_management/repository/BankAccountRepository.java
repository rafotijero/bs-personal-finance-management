package app.rafo.bs_personal_finance_management.repository;

import app.rafo.bs_personal_finance_management.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    List<BankAccount> findByBankId(Long bankId);

    List<BankAccount> findByOwnerId(Long ownerId);

    List<BankAccount> findByOwnerIdAndIsDeleted(Long ownerId, Character isDeleted);

    List<BankAccount> findByIsDeleted(Character isDeleted);
}
