package app.rafo.bs_personal_finance_management.service.impl;

import app.rafo.bs_personal_finance_management.auth.AuthUtil;
import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.TransactionDTO;
import app.rafo.bs_personal_finance_management.model.*;
import app.rafo.bs_personal_finance_management.repository.BankAccountRepository;
import app.rafo.bs_personal_finance_management.repository.TransactionRepository;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import app.rafo.bs_personal_finance_management.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ApiResponse<TransactionDTO> createTransaction(TransactionDTO transactionDTO) {
        BankAccount bankAccount = bankAccountRepository.findById(transactionDTO.getBankAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Bank account not found"));

        TransactionType transactionType = TransactionType.valueOf(transactionDTO.getTransactionType());

        Transaction transaction = Transaction.builder()
                .bankAccount(bankAccount)
                .transactionType(transactionType)
                .amount(transactionDTO.getAmount())
                .transactionDate(transactionDTO.getTransactionDate() != null ?
                        transactionDTO.getTransactionDate() : LocalDateTime.now())
                .description(transactionDTO.getDescription())
                .receiptFilePath(transactionDTO.getReceiptFilePath())
                .isDeleted('0')
                .createdAt(LocalDateTime.now())
                .createdBy(AuthUtil.getAuthenticatedUser()) // Reemplazar con el usuario autenticado
                .build();

        transaction = transactionRepository.save(transaction);

        return new ApiResponse<>(mapToDTO(transaction), "Transaction created successfully", 201, 1);
    }

    @Override
    public ApiResponse<TransactionDTO> getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        return new ApiResponse<>(mapToDTO(transaction), "Transaction retrieved successfully", 200, 1);
    }

    @Override
    public ApiResponse<List<TransactionDTO>> getTransactionsByBankAccount(Long bankAccountId) {
        List<TransactionDTO> transactions = transactionRepository.findByBankAccountIdAndIsDeleted(bankAccountId, '0')
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ApiResponse<>(transactions, "Transactions retrieved successfully", 200, transactions.size());
    }

    @Override
    public ApiResponse<List<TransactionDTO>> getTransactionsByUser(Long userId) {
        // Obtener el usuario autenticado
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));

        // Si el usuario es ADMIN, puede ver cualquier transacción
        if (authenticatedUser.getRole().equals(UserRole.ADMIN)) {
            List<TransactionDTO> transactions = transactionRepository.findByBankAccount_Owner_IdAndIsDeleted(userId, '0')
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            return new ApiResponse<>(transactions, "User transactions retrieved successfully", 200, transactions.size());
        }

        // Si el usuario es USER, solo puede ver sus propias transacciones
        if (!authenticatedUser.getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to view these transactions");
        }

        // Retornar las transacciones si la validación es exitosa
        List<TransactionDTO> transactions = transactionRepository.findByBankAccount_Owner_IdAndIsDeleted(userId, '0')
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ApiResponse<>(transactions, "User transactions retrieved successfully", 200, transactions.size());
    }


    @Override
    public ApiResponse<List<TransactionDTO>> getTransactionsByType(Long bankAccountId, String transactionType) {
        TransactionType type = TransactionType.valueOf(transactionType);
        List<TransactionDTO> transactions = transactionRepository
                .findByBankAccountIdAndTransactionTypeAndIsDeleted(bankAccountId, type, '0')
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ApiResponse<>(transactions, "Transactions retrieved successfully", 200, transactions.size());
    }

    @Override
    @Transactional
    public ApiResponse<TransactionDTO> updateTransaction(Long id, TransactionDTO transactionDTO) {
        // Buscar la transacción por ID
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        // Obtener usuario autenticado
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));

        // Si el usuario es USER, validar que solo pueda modificar sus propias transacciones
        if (authenticatedUser.getRole().equals(UserRole.USER) &&
                !transaction.getBankAccount().getOwner().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You are not authorized to update this transaction");
        }

        // Actualizar solo los campos modificados (no sobrescribir con null)
        if (transactionDTO.getAmount() != null) {
            transaction.setAmount(transactionDTO.getAmount());
        }
        if (transactionDTO.getTransactionDate() != null) {
            transaction.setTransactionDate(transactionDTO.getTransactionDate());
        }
        if (transactionDTO.getDescription() != null) {
            transaction.setDescription(transactionDTO.getDescription());
        }
        if (transactionDTO.getReceiptFilePath() != null) {
            transaction.setReceiptFilePath(transactionDTO.getReceiptFilePath());
        }

        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setUpdatedBy(authenticatedEmail); // Registrar quién actualizó la transacción

        // Guardar los cambios
        transaction = transactionRepository.save(transaction);

        return new ApiResponse<>(mapToDTO(transaction), "Transaction updated successfully", 200, 1);
    }


    @Override
    @Transactional
    public ApiResponse<Void> deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        transaction.setIsDeleted('1');
        transaction.setDeletedAt(LocalDateTime.now());
        transaction.setDeletedBy(AuthUtil.getAuthenticatedUser()); // Reemplazar con usuario autenticado

        transactionRepository.save(transaction);

        return new ApiResponse<>(null, "Transaction deleted successfully", 204, 0);
    }

    // Método de mapeo de Entidad -> DTO
    private TransactionDTO mapToDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .bankAccountId(transaction.getBankAccount().getId())
                .transactionType(transaction.getTransactionType().name())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .description(transaction.getDescription())
                .receiptFilePath(transaction.getReceiptFilePath())
                .build();
    }
}
