package app.rafo.bs_personal_finance_management.repository;

import app.rafo.bs_personal_finance_management.model.BankAccount;
import app.rafo.bs_personal_finance_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByOwner(User owner); // Obtener cuentas de un usuario
}
