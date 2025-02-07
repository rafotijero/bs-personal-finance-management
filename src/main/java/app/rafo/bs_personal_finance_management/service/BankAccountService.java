package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.model.Bank;
import app.rafo.bs_personal_finance_management.model.BankAccount;
import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.repository.BankAccountRepository;
import app.rafo.bs_personal_finance_management.repository.BankRepository;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    public ApiResponse<List<BankAccount>> getAllBankAccounts() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        return new ApiResponse<>(accounts, "List of all bank accounts", 200, accounts.size());
    }

    public ApiResponse<List<BankAccount>> getAccountsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<BankAccount> accounts = bankAccountRepository.findByOwner(user);
        return new ApiResponse<>(accounts, "List of bank accounts for user", 200, accounts.size());
    }

    public BankAccount createBankAccount(BankAccount bankAccount, Long bankId, Long userId) {
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new RuntimeException("Bank not found with ID: " + bankId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        bankAccount.setBank(bank);
        bankAccount.setOwner(user);

        return bankAccountRepository.save(bankAccount);
    }
}
