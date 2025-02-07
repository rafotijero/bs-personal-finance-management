package app.rafo.bs_personal_finance_management.controller;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.model.FixedIncome;
import app.rafo.bs_personal_finance_management.service.FixedIncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fixed-incomes")
@RequiredArgsConstructor
public class FixedIncomeController {

    private final FixedIncomeService fixedIncomeService;

    @PostMapping
    public ResponseEntity<ApiResponse<FixedIncome>> createFixedIncome(
            @RequestBody FixedIncome fixedIncome, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fixedIncomeService.createFixedIncome(fixedIncome, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FixedIncome>>> getUserFixedIncomes(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fixedIncomeService.getUserFixedIncomes(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FixedIncome>> getFixedIncomeById(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fixedIncomeService.getFixedIncomeById(id, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFixedIncome(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(fixedIncomeService.deleteFixedIncome(id, userDetails.getUsername()));
    }
}
