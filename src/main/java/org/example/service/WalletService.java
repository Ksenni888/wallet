package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.WalletNotFoundException;
import org.example.exceptions.WalletValidationException;
import org.example.model.OperationType;
import org.example.model.Wallet;
import org.example.storage.impl.dao.WalletDbStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {
    @Autowired
    private WalletDbStorage walletDbStorage;
    public double changeBalance(Wallet wallet){
        validateWallet(wallet);
        return walletDbStorage.changeBalance(wallet);
    }
    public double getBalance(int id) {
        validateIdWallet(id);
        return walletDbStorage.getBalance(id);
    }


    private void validateIdWallet(int id) {
        if (id <= 0) {
            throw new WalletNotFoundException("Неправильный номер кошелька.");
        }
        if (!walletDbStorage.findWalletId(id)) {
            throw new WalletNotFoundException(String.format("Кошелек с id = \"%d\" не найден", id));
        }
    }
    private void validateWallet(Wallet wallet) {
        validateIdWallet(wallet.getId());
        if (wallet.getOperationType() == null) {
            throw new WalletValidationException("В запросе отсутствует название операции - operation type - DEPOSIT/WITHDRAW");
        }
        if (wallet.getOperationType().isBlank()) {
            throw new WalletValidationException("В запросе отсутствует название операции - operation type - DEPOSIT/WITHDRAW");
        }
        if (!OperationType.findByName(wallet.getOperationType())) {
            throw new WalletValidationException("В запросе отсутствует название операции - operation type - DEPOSIT/WITHDRAW");
        }

        if ((wallet.getAmount() <= 0)) {
            throw new WalletValidationException("Cумма для проведения операции не может быть меньше 0");
        }

    }
}
