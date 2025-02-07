package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.model.Transaction;
import app.rafo.bs_personal_finance_management.model.TransactionType;
import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.model.UserRole;
import app.rafo.bs_personal_finance_management.repository.TransactionRepository;
import app.rafo.bs_personal_finance_management.repository.BankAccountRepository;
import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    /**
     * Retrieve all transactions.
     */
    public ApiResponse<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return new ApiResponse<>(transactions, "Transactions retrieved successfully", HttpStatus.OK.value(), transactions.size());
    }

    public Optional<Transaction> getTransactionById(Long id, String authenticatedEmail) {
        Optional<Transaction> transaction = transactionRepository.findById(id);

        // 🔥 Si el usuario es USER, solo puede ver sus propias transacciones
        if (transaction.isPresent()) {
            User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (authenticatedUser.getRole() == UserRole.USER &&
                    !transaction.get().getBankAccount().getOwner().getId().equals(authenticatedUser.getId())) {
                throw new RuntimeException("Unauthorized: You can only view your own transactions.");
            }
        }

        return transaction;
    }

    /**
     * Retrieve transactions by bank account.
     */
    public ApiResponse<List<Transaction>> getTransactionsByBankAccount(Long bankAccountId) {
        List<Transaction> transactions = transactionRepository.findByBankAccountId(bankAccountId);
        return new ApiResponse<>(transactions, "Transactions retrieved successfully", HttpStatus.OK.value(), transactions.size());
    }

    /**
     * Retrieve transactions by type (INCOME or EXPENSE).
     */
    public ApiResponse<List<Transaction>> getTransactionsByType(Long bankAccountId, TransactionType type) {
        List<Transaction> transactions = transactionRepository.findByBankAccountIdAndTransactionType(bankAccountId, type);
        return new ApiResponse<>(transactions, "Transactions retrieved successfully", HttpStatus.OK.value(), transactions.size());
    }

    /**
     * Create a new transaction.
     */
    public Transaction createTransaction(Transaction transaction, String authenticatedEmail) {
        User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Si el usuario es USER, solo puede registrar transacciones para sí mismo
        if (authenticatedUser.getRole() == UserRole.USER && !transaction.getBankAccount().getOwner().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("Unauthorized: You can only create transactions for yourself.");
        }

        return transactionRepository.save(transaction);
    }

    /**
     * Delete a transaction.
     */
    public ApiResponse<Void> deleteTransaction(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            transactionRepository.deleteById(id);
            return new ApiResponse<>(null, "Transaction deleted successfully", HttpStatus.OK.value(), 0);
        }
        return new ApiResponse<>(null, "Transaction not found", HttpStatus.NOT_FOUND.value(), 0);
    }
}
