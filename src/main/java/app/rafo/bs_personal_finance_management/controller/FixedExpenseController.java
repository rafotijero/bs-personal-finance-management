package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.model.FixedExpense;
import app.rafo.bs_personal_finance_management.service.FixedExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fixed-expenses")
@RequiredArgsConstructor
public class FixedExpenseController {

    private final FixedExpenseService fixedExpenseService;

    @PostMapping
    public ResponseEntity<ApiResponse<FixedExpense>> createFixedExpense(
            @RequestBody FixedExpense fixedExpense, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fixedExpenseService.createFixedExpense(fixedExpense, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FixedExpense>>> getUserFixedExpenses(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fixedExpenseService.getUserFixedExpenses(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FixedExpense>> getFixedExpenseById(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fixedExpenseService.getFixedExpenseById(id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFixedExpense(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fixedExpenseService.deleteFixedExpense(id, userDetails.getUsername()));
    }
}