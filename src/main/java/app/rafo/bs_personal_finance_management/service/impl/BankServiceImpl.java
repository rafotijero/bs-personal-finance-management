package app.rafo.bs_personal_finance_management.service.impl;

import app.rafo.bs_personal_finance_management.auth.AuthUtil;
import app.rafo.bs_personal_finance_management.dto.ApiResponse;
import app.rafo.bs_personal_finance_management.dto.BankDTO;
import app.rafo.bs_personal_finance_management.model.Bank;
import app.rafo.bs_personal_finance_management.repository.BankRepository;
import app.rafo.bs_personal_finance_management.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Override
    public ApiResponse<List<Bank>> getAllBanks() {
        List<Bank> banks = bankRepository.findAllByIsDeleted('0'); // 🔹 Solo bancos activos
        return new ApiResponse<>(banks, "List of all active banks", 200, banks.size());
    }

    @Override
    public ApiResponse<Bank> getBankById(Long id) {
        return bankRepository.findById(id)
                .filter(bank -> bank.getIsDeleted().equals('0')) // 🔹 Solo retorna si no está eliminado
                .map(bank -> new ApiResponse<>(bank, "Bank found", 200, 1))
                .orElse(new ApiResponse<>(null, "Bank not found or deleted", 404, 0));
    }

    @Override
    public ApiResponse<Bank> saveBank(BankDTO bankDTO) {
        // Verificar si ya existe un banco con las mismas iniciales activas
        if (bankRepository.existsByInitialsAndIsDeleted(bankDTO.getInitials(), '0')) {
            return new ApiResponse<>(null, "Bank with these initials already exists", 400, 0);
        }

        Bank bank = Bank.builder()
                .name(bankDTO.getName())
                .country(bankDTO.getCountry())
                .initials(bankDTO.getInitials()) // ✅ Guardar iniciales
                .logo(bankDTO.getLogo())
                .isDeleted('0') // 🔥 Nuevo banco siempre inicia como activo
                .createdBy(AuthUtil.getAuthenticatedUser())
                .createdAt(LocalDateTime.now())
                .build();

        Bank savedBank = bankRepository.save(bank);
        return new ApiResponse<>(savedBank, "Bank created successfully", 201, 1);
    }

    @Override
    public ApiResponse<Void> deleteBank(Long id) {
        Optional<Bank> bankOpt = bankRepository.findById(id);

        if (bankOpt.isEmpty()) {
            return new ApiResponse<>(null, "Bank not found", 404, 0);
        }

        Bank bank = bankOpt.get();

        if (bank.getIsDeleted().equals('1')) {
            return new ApiResponse<>(null, "Bank already deleted", 400, 0);
        }

        bank.setIsDeleted('1');
        bank.setDeletedAt(LocalDateTime.now());
        bank.setDeletedBy(AuthUtil.getAuthenticatedUser());

        bankRepository.save(bank);
        return new ApiResponse<>(null, "Bank deleted successfully", 200, 0);
    }

    @Override
    public ApiResponse<Bank> updateBank(Long id, BankDTO bankDTO) {
        Optional<Bank> bankOpt = bankRepository.findById(id);

        if (bankOpt.isEmpty() || bankOpt.get().getIsDeleted().equals('1')) {
            return new ApiResponse<>(null, "Bank not found or deleted", 404, 0);
        }

        Bank bank = bankOpt.get();
        bank.setName(bankDTO.getName());
        bank.setCountry(bankDTO.getCountry());
        bank.setInitials(bankDTO.getInitials());
        bank.setLogo(bankDTO.getLogo() != null ? bankDTO.getLogo() : bank.getLogo());
        bank.setUpdatedAt(LocalDateTime.now());
        bank.setUpdatedBy(AuthUtil.getAuthenticatedUser()); // 🔥 Aquí usamos el usuario autenticado

        Bank updatedBank = bankRepository.save(bank);
        return new ApiResponse<>(updatedBank, "Bank updated successfully", 200, 1);
    }

    @Override
    public ApiResponse<Void> restoreBank(Long id) {
        Optional<Bank> bankOpt = bankRepository.findById(id);

        if (bankOpt.isEmpty() || bankOpt.get().getIsDeleted().equals('0')) {
            return new ApiResponse<>(null, "Bank not found or not deleted", 404, 0);
        }

        Bank bank = bankOpt.get();
        bank.setIsDeleted('0'); // 🔥 Marcar como activo
        bank.setDeletedAt(null);
        bank.setDeletedBy(null);
        bank.setUpdatedAt(LocalDateTime.now());
        bank.setUpdatedBy(AuthUtil.getAuthenticatedUser());

        bankRepository.save(bank);

        // ✅ Respuesta corregida
        return new ApiResponse<>(null, "Bank restored successfully", 200, 0);
    }
}
