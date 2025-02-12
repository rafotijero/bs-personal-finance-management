package app.rafo.bs_personal_finance_management.service.impl;

import app.rafo.bs_personal_finance_management.auth.AuthUtil;
import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.BankAccountDTO;
import app.rafo.bs_personal_finance_management.model.AccountType;
import app.rafo.bs_personal_finance_management.model.Bank;
import app.rafo.bs_personal_finance_management.model.BankAccount;
import app.rafo.bs_personal_finance_management.model.User;
import app.rafo.bs_personal_finance_management.repository.BankAccountRepository;
import app.rafo.bs_personal_finance_management.repository.BankRepository;
import app.rafo.bs_personal_finance_management.repository.UserRepository;
import app.rafo.bs_personal_finance_management.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankRepository bankRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ApiResponse<BankAccountDTO> createBankAccount(BankAccountDTO bankAccountDTO) {
        Bank bank = bankRepository.findById(bankAccountDTO.getBankId())
                .orElseThrow(() -> new RuntimeException("Bank not found"));

        User owner = userRepository.findById(bankAccountDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        AccountType accountType;
        try {
            accountType = AccountType.valueOf(bankAccountDTO.getAccountType().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(null, "Invalid account type", 400, 0);
        }

        BankAccount bankAccount = BankAccount.builder()
                .accountDescription(bankAccountDTO.getAccountDescription())
                .accountNumber(bankAccountDTO.getAccountNumber())
                .balance(bankAccountDTO.getBalance())
                .accountType(accountType)
                .bank(bank)
                .owner(owner)
                .isDeleted('0') // Nueva cuenta siempre activa
                .createdAt(LocalDateTime.now())
                .createdBy(AuthUtil.getAuthenticatedUser())
                .build();

        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return new ApiResponse<>(mapToDTO(savedBankAccount), "Bank account created successfully", 201, 1);
    }

    @Override
    public ApiResponse<BankAccountDTO> getBankAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .filter(bankAccount -> bankAccount.getIsDeleted().equals('0')) // Solo cuentas activas
                .map(bankAccount -> new ApiResponse<>(mapToDTO(bankAccount), "Bank account found", 200, 1))
                .orElse(new ApiResponse<>(null, "Bank account not found or deleted", 404, 0));
    }

    @Override
    public ApiResponse<List<BankAccountDTO>> getAllBankAccounts() {
        List<BankAccountDTO> accounts = bankAccountRepository.findByIsDeleted('0')
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(accounts, "List of all active bank accounts", 200, accounts.size());
    }

    @Override
    public ApiResponse<List<BankAccountDTO>> getBankAccountsByBankId(Long bankId) {
        List<BankAccountDTO> accounts = bankAccountRepository.findByBankId(bankId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(accounts, "List of bank accounts for the given bank", 200, accounts.size());
    }

    @Override
    public ApiResponse<List<BankAccountDTO>> getBankAccountsByOwnerId(Long ownerId) {
        List<BankAccountDTO> accounts = bankAccountRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return new ApiResponse<>(accounts, "List of bank accounts for the given owner", 200, accounts.size());
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteBankAccount(Long id) {
        Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(id);

        if (bankAccountOpt.isEmpty()) {
            return new ApiResponse<>(null, "Bank account not found", 404, 0);
        }

        BankAccount bankAccount = bankAccountOpt.get();

        if (bankAccount.getIsDeleted().equals('1')) {
            return new ApiResponse<>(null, "Bank account already deleted", 400, 0);
        }

        bankAccount.setIsDeleted('1');
        bankAccount.setDeletedAt(LocalDateTime.now());
        bankAccount.setDeletedBy(AuthUtil.getAuthenticatedUser());

        bankAccountRepository.save(bankAccount);
        return new ApiResponse<>(null, "Bank account deleted successfully", 200, 0);
    }

    @Override
    @Transactional
    public ApiResponse<Void> restoreBankAccount(Long id) {
        Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(id);

        if (bankAccountOpt.isEmpty() || bankAccountOpt.get().getIsDeleted().equals('0')) {
            return new ApiResponse<>(null, "Bank account not found or not deleted", 404, 0);
        }

        BankAccount bankAccount = bankAccountOpt.get();
        bankAccount.setIsDeleted('0');
        bankAccount.setDeletedAt(null);
        bankAccount.setDeletedBy(null);
        bankAccount.setUpdatedAt(LocalDateTime.now());
        bankAccount.setUpdatedBy(AuthUtil.getAuthenticatedUser());

        bankAccountRepository.save(bankAccount);

        return new ApiResponse<>(null, "Bank account restored successfully", 200, 0);
    }

    private BankAccountDTO mapToDTO(BankAccount bankAccount) {
        return BankAccountDTO.builder()
                .id(bankAccount.getId())
                .accountDescription(bankAccount.getAccountDescription())
                .accountNumber(bankAccount.getAccountNumber())
                .balance(bankAccount.getBalance())
                .accountType(bankAccount.getAccountType().name()) // Convertir Enum a String
                .bankId(bankAccount.getBank().getId()) // Seguimos devolviendo bankId
                .bank(bankAccount.getBank())  // ✅ Ahora incluimos el objeto `bank`
                .ownerId(bankAccount.getOwner().getId()) // Seguimos devolviendo ownerId
                .owner(bankAccount.getOwner())  // ✅ Ahora incluimos el objeto `owner`
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<BankAccountDTO> updateBankAccount(Long id, BankAccountDTO bankAccountDTO) {
        Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(id);

        if (bankAccountOpt.isEmpty() || bankAccountOpt.get().getIsDeleted().equals('1')) {
            return new ApiResponse<>(null, "Bank account not found or deleted", 404, 0);
        }

        BankAccount bankAccount = bankAccountOpt.get();

        // Validar si se intenta cambiar a un banco que no existe
        if (bankAccountDTO.getBankId() != null) {
            Bank bank = bankRepository.findById(bankAccountDTO.getBankId())
                    .orElseThrow(() -> new RuntimeException("Bank not found"));
            bankAccount.setBank(bank);
        }

        // Validar si se intenta cambiar el usuario propietario
        if (bankAccountDTO.getOwnerId() != null) {
            User owner = userRepository.findById(bankAccountDTO.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            bankAccount.setOwner(owner);
        }

        // Si se envía una descripción de cuenta, actualizarlo
        if (bankAccountDTO.getAccountDescription() != null) {
            bankAccount.setAccountDescription(bankAccountDTO.getAccountDescription());
        }

        // Si se envía un número de cuenta, actualizarlo
        if (bankAccountDTO.getAccountNumber() != null) {
            bankAccount.setAccountNumber(bankAccountDTO.getAccountNumber());
        }

        // Si se envía un nuevo saldo, actualizarlo
        if (bankAccountDTO.getBalance() != null) {
            bankAccount.setBalance(bankAccountDTO.getBalance());
        }

        // Si se envía un tipo de cuenta, convertirlo de String a Enum
        if (bankAccountDTO.getAccountType() != null) {
            try {
                AccountType accountType = AccountType.valueOf(bankAccountDTO.getAccountType().toUpperCase());
                bankAccount.setAccountType(accountType);
            } catch (IllegalArgumentException e) {
                return new ApiResponse<>(null, "Invalid account type", 400, 0);
            }
        }

        bankAccount.setUpdatedAt(LocalDateTime.now());
        bankAccount.setUpdatedBy(AuthUtil.getAuthenticatedUser());

        BankAccount updatedBankAccount = bankAccountRepository.save(bankAccount);
        return new ApiResponse<>(mapToDTO(updatedBankAccount), "Bank account updated successfully", 200, 1);
    }

}
