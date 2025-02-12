package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.TransactionDTO;
import app.rafo.bs_personal_finance_management.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // 🔹 Crear transacción
    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDTO>> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.createTransaction(transactionDTO));
    }

    // 🔹 Obtener transacción por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    // 🔹 Listar transacciones por cuenta bancaria
    @GetMapping("/bank-account/{bankAccountId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByBankAccount(@PathVariable Long bankAccountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBankAccount(bankAccountId));
    }

    // 🔹 Listar transacciones por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }

    // 🔹 Listar transacciones por tipo (INCOME o EXPENSE) en una cuenta específica
    @GetMapping("/bank-account/{bankAccountId}/type/{transactionType}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByType(@PathVariable Long bankAccountId,
                                                                                   @PathVariable String transactionType) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(bankAccountId, transactionType));
    }

    // 🔹 Actualizar transacción
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> updateTransaction(@PathVariable Long id,
                                                                         @Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDTO));
    }

    // 🔹 Eliminar transacción (eliminación lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.deleteTransaction(id));
    }
}
