package app.rafo.bs_personal_finance_management.service;

import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {

    ApiResponse<TransactionDTO> createTransaction(TransactionDTO transactionDTO); // Crear transacción

    ApiResponse<TransactionDTO> getTransactionById(Long id); // Obtener transacción por ID

    ApiResponse<List<TransactionDTO>> getTransactionsByBankAccount(Long bankAccountId); // Listar por cuenta bancaria

    ApiResponse<List<TransactionDTO>> getTransactionsByUser(Long userId); // Listar por usuario

    ApiResponse<List<TransactionDTO>> getTransactionsByType(Long bankAccountId, String transactionType); // Listar por tipo

    ApiResponse<TransactionDTO> updateTransaction(Long id, TransactionDTO transactionDTO); // Actualizar transacción

    ApiResponse<Void> deleteTransaction(Long id); // Eliminación lógica
}
