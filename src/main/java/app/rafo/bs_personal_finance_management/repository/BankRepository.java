package app.rafo.bs_personal_finance_management.repository;

import app.rafo.bs_personal_finance_management.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findByName(String name);
}
