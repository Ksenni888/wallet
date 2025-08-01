package org.example.storage.interfaces;

import org.example.model.Wallet;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


public interface WalletStorage {
    double changeBalance(Wallet wallet);
    double getBalance(int id);
    boolean findWalletId(int id);
    }

