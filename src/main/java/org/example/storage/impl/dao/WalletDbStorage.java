package org.example.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.example.model.Wallet;
import org.example.storage.interfaces.WalletStorage;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Primary
@RequiredArgsConstructor
public class WalletDbStorage implements WalletStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public double changeBalance(Wallet wallet){
        String sqlGetAmount =
                String.format("SELECT amount FROM wallet WHERE wallet_id = %d", wallet.getId());
        double amount = Double.parseDouble(String.valueOf(jdbcTemplate.queryForObject(sqlGetAmount, BigDecimal.class)));

        if ("DEPOSIT".equals(wallet.getOperationType())) {
            amount = amount + wallet.getAmount();
            jdbcTemplate.update("UPDATE wallet SET amount = ?, operation_type = ? WHERE wallet_id = ?", amount, wallet.getOperationType(), wallet.getId());
        }
        if ("WITHDRAW".equals(wallet.getOperationType().toString())) {
            amount = amount - wallet.getAmount();
            jdbcTemplate.update("UPDATE wallet SET amount = ?, operation_type = ? WHERE wallet_id = ?", amount, wallet.getOperationType(), wallet.getId());
        }
        return amount;
    }

    @Override
    public double getBalance(int id) {
        String sqlGetAmount =
                String.format("SELECT amount FROM wallet WHERE wallet_id = %d", id);
        System.out.println(id);
       return Double.parseDouble(String.valueOf(jdbcTemplate.queryForObject(sqlGetAmount, BigDecimal.class)));
    }

    @Override
    public boolean findWalletId(int id) {
        SqlRowSet walletRows = jdbcTemplate.queryForRowSet("SELECT * FROM wallet WHERE wallet_id = ?", id);
        return walletRows.next();
    }
}
