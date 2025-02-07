package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.model.FixedExpense;
import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.repository.FixedExpenseRepository;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FixedExpenseService {

    private final FixedExpenseRepository fixedExpenseRepository;
    private final UserRepository userRepository;

    public ApiResponse<FixedExpense> createFixedExpense(FixedExpense fixedExpense, String authenticatedEmail) {
        User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Solo el usuario autenticado puede registrar su propio gasto fijo
        fixedExpense.setUser(authenticatedUser);

        FixedExpense savedExpense = fixedExpenseRepository.save(fixedExpense);
        return new ApiResponse<>(savedExpense, "Fixed Expense created successfully", 201, 1);
    }

    public ApiResponse<List<FixedExpense>> getUserFixedExpenses(String authenticatedEmail) {
        User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FixedExpense> expenses = fixedExpenseRepository.findByUser(authenticatedUser);
        return new ApiResponse<>(expenses, "Fixed Expenses retrieved successfully", 200, expenses.size());
    }

    public ApiResponse<FixedExpense> getFixedExpenseById(Long id, String authenticatedEmail) {
        FixedExpense fixedExpense = fixedExpenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fixed Expense not found"));

        if (!fixedExpense.getUser().getEmail().equals(authenticatedEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        return new ApiResponse<>(fixedExpense, "Fixed Expense retrieved successfully", 200, 1);
    }

    public ApiResponse<String> deleteFixedExpense(Long id, String authenticatedEmail) {
        FixedExpense fixedExpense = fixedExpenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fixed Expense not found"));

        if (!fixedExpense.getUser().getEmail().equals(authenticatedEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        fixedExpenseRepository.delete(fixedExpense);
        return new ApiResponse<>(null, "Fixed Expense deleted successfully", 200, 0);
    }
}
