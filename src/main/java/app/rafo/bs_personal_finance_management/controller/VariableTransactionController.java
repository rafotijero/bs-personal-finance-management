package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.model.VariableTransaction;
import app.rafo.bs_personal_finance_management.service.VariableTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/variable-transactions")
@RequiredArgsConstructor
public class VariableTransactionController {

    private final VariableTransactionService variableTransactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<VariableTransaction>> createVariableTransaction(
            @RequestBody VariableTransaction transaction) {

        // 🚨 Agregar log para verificar usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(null, "User is not authenticated", 401, 0));
        }

        String authenticatedEmail = authentication.getName();
        VariableTransaction savedTransaction = variableTransactionService.createVariableTransaction(transaction, authenticatedEmail);

        ApiResponse<VariableTransaction> response = new ApiResponse<>(savedTransaction, "Transaction created successfully", 201, 1);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VariableTransaction>>> getAllVariableTransactions() {
        List<VariableTransaction> transactions = variableTransactionService.getAllVariableTransactions();
        ApiResponse<List<VariableTransaction>> response = new ApiResponse<>(transactions, "Transactions retrieved successfully", 200, transactions.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VariableTransaction>> getVariableTransactionById(@PathVariable Long id) {
        return variableTransactionService.getVariableTransactionById(id)
                .map(transaction -> ResponseEntity.ok(new ApiResponse<>(transaction, "Transaction found", 200, 1)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(null, "Transaction not found", 404, 0)));
    }
}