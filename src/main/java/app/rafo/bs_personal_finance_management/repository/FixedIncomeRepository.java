package app.rafo.bs_personal_finance_management.repository;

import app.rafo.bs_personal_finance_management.model.FixedIncome;
import app.rafo.bs_personal_finance_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixedIncomeRepository extends JpaRepository<FixedIncome, Long> {
    List<FixedIncome> findByUser(User user);
}
