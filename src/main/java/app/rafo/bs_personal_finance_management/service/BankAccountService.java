package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.BankAccountDTO;

import java.util.List;

public interface BankAccountService {

    ApiResponse<BankAccountDTO> createBankAccount(BankAccountDTO bankAccountDTO);

    ApiResponse<BankAccountDTO> getBankAccountById(Long id);

    ApiResponse<List<BankAccountDTO>> getAllBankAccounts();

    ApiResponse<List<BankAccountDTO>> getBankAccountsByBankId(Long bankId);

    ApiResponse<List<BankAccountDTO>> getBankAccountsByOwnerId(Long ownerId);

    ApiResponse<Void> deleteBankAccount(Long id);

    ApiResponse<Void> restoreBankAccount(Long id);

    ApiResponse<BankAccountDTO> updateBankAccount(Long id, BankAccountDTO bankAccountDTO);

}
