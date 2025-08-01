package org.example.storage.interfaces;

import org.example.model.Wallet;

public interface WalletStorage {
    double changeBalance(Wallet wallet);
    double getBalance(int id);
    boolean findWalletId(int id);
}

