package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.model.BankAccount;
import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.model.UserRole;
import app.rafo.bs_personal_finance_management.model.VariableTransaction;
import app.rafo.bs_personal_finance_management.repository.BankAccountRepository;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import app.rafo.bs_personal_finance_management.repository.VariableTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VariableTransactionService {

    private final VariableTransactionRepository variableTransactionRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public VariableTransaction createVariableTransaction(VariableTransaction transaction, String authenticatedEmail) {
        User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Cargar el BankAccount desde la base de datos
        BankAccount bankAccount = bankAccountRepository.findById(transaction.getBankAccount().getId())
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));

        // ✅ Ahora sí tiene un owner
        if (authenticatedUser.getRole() == UserRole.USER && !bankAccount.getOwner().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("Unauthorized: You can only create transactions for your own account.");
        }

        // ✅ Asignar el BankAccount completo con Owner
        transaction.setBankAccount(bankAccount);

        return variableTransactionRepository.save(transaction);
    }


    public List<VariableTransaction> getAllVariableTransactions() {
        return variableTransactionRepository.findAll();
    }

    public Optional<VariableTransaction> getVariableTransactionById(Long id) {
        return variableTransactionRepository.findById(id);
    }

    public List<VariableTransaction> getTransactionsByBankAccount(Long bankAccountId) {
        return variableTransactionRepository.findByBankAccount_Id(bankAccountId);
    }
}

