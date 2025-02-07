package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.BankAccountRequest;
import app.rafo.bs_personal_finance_management.model.AccountType;
import app.rafo.bs_personal_finance_management.model.BankAccount;
import app.rafo.bs_personal_finance_management.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-accounts") // 🔹 Se ajusta para que el context-path "/api/v1" lo agregue automáticamente
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BankAccount>>> getAllBankAccounts() {
        return ResponseEntity.ok(bankAccountService.getAllBankAccounts());
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<ApiResponse<List<BankAccount>>> getUserAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bankAccountService.getAccountsByUser(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BankAccount>> createBankAccount(@RequestBody BankAccountRequest request) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(request.getAccountNumber());
        bankAccount.setAccountType(AccountType.valueOf(request.getAccountType().toUpperCase())); // ✅ Convierte String a Enum
        bankAccount.setBalance(request.getBalance());

        BankAccount savedAccount = bankAccountService.createBankAccount(bankAccount, request.getBankId(), request.getUserId());

        return ResponseEntity.ok(new ApiResponse<>(savedAccount, "Bank account created successfully", 201, 1));
    }

}
