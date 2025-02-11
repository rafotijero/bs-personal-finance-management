package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.BankAccountDTO;
import app.rafo.bs_personal_finance_management.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<ApiResponse<BankAccountDTO>> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        ApiResponse<BankAccountDTO> response = bankAccountService.createBankAccount(bankAccountDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BankAccountDTO>> getBankAccountById(@PathVariable Long id) {
        ApiResponse<BankAccountDTO> response = bankAccountService.getBankAccountById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BankAccountDTO>>> getAllBankAccounts() {
        ApiResponse<List<BankAccountDTO>> response = bankAccountService.getAllBankAccounts();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/bank/{bankId}")
    public ResponseEntity<ApiResponse<List<BankAccountDTO>>> getBankAccountsByBankId(@PathVariable Long bankId) {
        ApiResponse<List<BankAccountDTO>> response = bankAccountService.getBankAccountsByBankId(bankId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<BankAccountDTO>>> getBankAccountsByOwnerId(@PathVariable Long ownerId) {
        ApiResponse<List<BankAccountDTO>> response = bankAccountService.getBankAccountsByOwnerId(ownerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBankAccount(@PathVariable Long id) {
        ApiResponse<Void> response = bankAccountService.deleteBankAccount(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreBankAccount(@PathVariable Long id) {
        ApiResponse<Void> response = bankAccountService.restoreBankAccount(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BankAccountDTO>> updateBankAccount(
            @PathVariable Long id,
            @RequestBody BankAccountDTO bankAccountDTO) {
        ApiResponse<BankAccountDTO> response = bankAccountService.updateBankAccount(id, bankAccountDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
