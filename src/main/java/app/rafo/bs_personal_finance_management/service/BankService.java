package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.model.Bank;
import app.rafo.bs_personal_finance_management.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;

    public ApiResponse<List<Bank>> getAllBanks() {
        List<Bank> banks = bankRepository.findAll();
        return new ApiResponse<>(banks, "List of all banks", 200, banks.size());
    }

    public ApiResponse<Bank> getBankById(Long id) {
        return bankRepository.findById(id)
                .map(bank -> new ApiResponse<>(bank, "Bank found", 200, 1))
                .orElse(new ApiResponse<>(null, "Bank not found", 404, 0));
    }

    public ApiResponse<Bank> saveBank(Bank bank) {
        Bank savedBank = bankRepository.save(bank);
        return new ApiResponse<>(savedBank, "Bank created successfully", 201, 1);
    }

    public ApiResponse<Void> deleteBank(Long id) {
        if (!bankRepository.existsById(id)) {
            return new ApiResponse<>(null, "Bank not found", 404, 0);
        }
        bankRepository.deleteById(id);
        return new ApiResponse<>(null, "Bank deleted successfully", 200, 0);
    }
}
