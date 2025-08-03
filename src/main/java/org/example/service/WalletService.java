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
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {
    @Autowired
    private WalletDbStorage walletDbStorage;
    @Transactional
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
            throw new WalletNotFoundException("Wrong wallet's number.");
        }
        if (!walletDbStorage.findWalletId(id)) {
            throw new WalletNotFoundException(String.format("Wallet with id = \"%d\" not found", id));
        }
    }
    private void validateWallet(Wallet wallet) {
        validateIdWallet(wallet.getId());
        if (wallet.getOperationType() == null) {
            throw new WalletValidationException("The operation name is missing in the request (operation type: DEPOSIT/WITHDRAW)");
        }
        if (wallet.getOperationType().isBlank()) {
            throw new WalletValidationException("The operation name is missing in the request (operation type: DEPOSIT/WITHDRAW)");
        }
        if (!OperationType.findByName(wallet.getOperationType())) {
            throw new WalletValidationException("The operation name is missing in the request (operation type: DEPOSIT/WITHDRAW)");
        }

        if ((wallet.getAmount() <= 0)) {
            throw new WalletValidationException("The amount for the operation cannot be less than 0");
        }
    }
}