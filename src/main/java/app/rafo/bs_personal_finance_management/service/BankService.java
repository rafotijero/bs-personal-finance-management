package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.BankDTO;
import app.rafo.bs_personal_finance_management.model.Bank;

import java.util.List;

public interface BankService {
    ApiResponse<List<Bank>> getAllBanks();
    ApiResponse<Bank> getBankById(Long id);
    ApiResponse<Bank> saveBank(BankDTO bankDTO);
    ApiResponse<Void> deleteBank(Long id); // Eliminación lógica
    ApiResponse<Bank> updateBank(Long id, BankDTO bankDTO);
    ApiResponse<Void> restoreBank(Long id);
}
