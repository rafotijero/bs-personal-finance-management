package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.model.Transaction;
import app.rafo.bs_personal_finance_management.model.TransactionType;
import app.rafo.bs_personal_finance_management.service.TransactionService;
import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<Transaction>>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("🔍 Fetching transaction with ID: {}", id);
        logger.info("👤 Authenticated User: {}", userDetails.getUsername());

        Transaction transaction = transactionService.getTransactionById(id, userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        ApiResponse<Transaction> response = new ApiResponse<>(
                transaction,
                "Transaction retrieved successfully",
                HttpStatus.OK.value(),
                1
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{bankAccountId}")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsByBankAccount(@PathVariable Long bankAccountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBankAccount(bankAccountId));
    }

    @GetMapping("/account/{bankAccountId}/type/{type}")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsByType(@PathVariable Long bankAccountId, @PathVariable TransactionType type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(bankAccountId, type));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(@RequestBody Transaction transaction,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        // ✅ Obtener el email del usuario autenticado desde el UserDetails
        String authenticatedEmail = userDetails.getUsername();

        // ✅ Llamar al servicio con la transacción y el email autenticado
        Transaction savedTransaction = transactionService.createTransaction(transaction, authenticatedEmail);

        // ✅ Retornar la respuesta con ApiResponse
        ApiResponse<Transaction> response = new ApiResponse<>(
                savedTransaction,
                "Transaction created successfully.",
                HttpStatus.CREATED.value(),
                1
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.deleteTransaction(id));
    }
}