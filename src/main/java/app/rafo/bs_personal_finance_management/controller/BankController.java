package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.model.Bank;
import app.rafo.bs_personal_finance_management.service.BankService;
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
        return ResponseEntity.status(bankService.getBankById(id).getStatusCode())
                .body(bankService.getBankById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Bank>> createBank(@RequestBody Bank bank) {
        return ResponseEntity.status(201).body(bankService.saveBank(bank));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBank(@PathVariable Long id) {
        return ResponseEntity.status(bankService.deleteBank(id).getStatusCode())
                .body(bankService.deleteBank(id));
    }
}