package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.BankDTO;
import app.rafo.bs_personal_finance_management.model.Bank;
import app.rafo.bs_personal_finance_management.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banks") // 🔹 Se ajusta para que el context-path "/api/v1" lo agregue automáticamente
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Bank>>> getAllBanks() {
        return ResponseEntity.ok(bankService.getAllBanks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Bank>> getBankById(@PathVariable Long id) {
        ApiResponse<Bank> response = bankService.getBankById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Bank>> createBank(@Valid @RequestBody BankDTO bankDTO) {
        ApiResponse<Bank> response = bankService.saveBank(bankDTO);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
        bankService.deleteBank(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 sin body
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Bank>> updateBank(@PathVariable Long id, @Valid @RequestBody BankDTO bankDTO) {
        ApiResponse<Bank> response = bankService.updateBank(id, bankDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreBank(@PathVariable Long id) {
        ApiResponse<Void> response = bankService.restoreBank(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}