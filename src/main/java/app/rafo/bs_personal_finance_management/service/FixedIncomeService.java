package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.model.FixedIncome;
import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.repository.FixedIncomeRepository;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FixedIncomeService {

    private final FixedIncomeRepository fixedIncomeRepository;
    private final UserRepository userRepository;

    public ApiResponse<FixedIncome> createFixedIncome(FixedIncome fixedIncome, String authenticatedEmail) {
        User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Solo el usuario autenticado puede registrar su propio ingreso fijo
        fixedIncome.setUser(authenticatedUser);

        FixedIncome savedIncome = fixedIncomeRepository.save(fixedIncome);
        return new ApiResponse<>(savedIncome, "Fixed Income created successfully", 201, 1);
    }

    public ApiResponse<List<FixedIncome>> getUserFixedIncomes(String authenticatedEmail) {
        User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FixedIncome> incomes = fixedIncomeRepository.findByUser(authenticatedUser);
        return new ApiResponse<>(incomes, "Fixed Incomes retrieved successfully", 200, incomes.size());
    }

    public ApiResponse<FixedIncome> getFixedIncomeById(Long id, String authenticatedEmail) {
        FixedIncome fixedIncome = fixedIncomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fixed Income not found"));

        if (!fixedIncome.getUser().getEmail().equals(authenticatedEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        return new ApiResponse<>(fixedIncome, "Fixed Income retrieved successfully", 200, 1);
    }

    public ApiResponse<String> deleteFixedIncome(Long id, String authenticatedEmail) {
        FixedIncome fixedIncome = fixedIncomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fixed Income not found"));

        if (!fixedIncome.getUser().getEmail().equals(authenticatedEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        fixedIncomeRepository.delete(fixedIncome);
        return new ApiResponse<>(null, "Fixed Income deleted successfully", 200, 0);
    }
}
