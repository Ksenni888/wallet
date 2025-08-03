//package org.example.storage.impl.dao;
//
//import lombok.RequiredArgsConstructor;
//import org.example.exceptions.WalletZeroException;
//import org.example.model.Wallet;
//import org.example.storage.interfaces.WalletStorage;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.sql.DataSource;
//import java.math.BigDecimal;
//import java.sql.*;
//import java.time.LocalDateTime;
//
//@Component
//@Primary
//@RequiredArgsConstructor
//public class WalletDbStorage implements WalletStorage {
//    private final JdbcTemplate jdbcTemplate;
//    private static final int MAX_RETRIES = 3;
//    private static final String LOCK_TIMEOUT = "SET lock_timeout = 5000";
//
//    @Override
//    @Transactional(
//            isolation = Isolation.SERIALIZABLE,
//            propagation = Propagation.REQUIRED,
//            rollbackFor = Exception.class
//    )
//    public double changeBalance(Wallet wallet) throws WalletZeroException {
//        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
//            conn.setAutoCommit(false);
//            conn.prepareStatement(LOCK_TIMEOUT).execute();
//            try {
////                conn.prepareStatement(LOCK_TIMEOUT).execute();
//
//                BigDecimal currentAmount = getCurrentAmountWithLock(conn, wallet.getId());
//
//                String sqlGetAmount =
//               String.format("SELECT amount FROM wallet WHERE wallet_id = %d", wallet.getId());
//       double amount = Double.parseDouble(String.valueOf(jdbcTemplate.queryForObject(sqlGetAmount, BigDecimal.class)));
//
//                if ("DEPOSIT".equals(wallet.getOperationType())) {
//                    amount = amount + wallet.getAmount();
//            jdbcTemplate.update("UPDATE wallet SET amount = ?, operation_type = ? WHERE wallet_id = ?", amount, wallet.getOperationType(), wallet.getId());
////                   updateBalance(conn, wallet.getId(), currentAmount.add(BigDecimal.valueOf(wallet.getAmount())));
//
//                } else if ("WITHDRAW".equals(wallet.getOperationType())) {
//                    if (BigDecimal.valueOf(wallet.getAmount()).compareTo(currentAmount) > 0) {
//                        throw new WalletZeroException("Недостаточно средств");
//                    }
//                    updateBalance(conn, wallet.getId(), currentAmount.subtract(BigDecimal.valueOf(wallet.getAmount())));
//                }
//
//                conn.commit();
//                return currentAmount.doubleValue();
//            } catch (SQLException e) {
//                conn.rollback();
//                throw new RuntimeException("Ошибка при обновлении баланса", e);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Ошибка подключения к базе данных", e);
//        }
//    }
//
//    private BigDecimal getCurrentAmountWithLock(Connection conn, int walletId) throws SQLException {
//        String sql = "    SELECT amount" +
//                     "    FROM wallet" +
//                     "    WHERE wallet_id = ?" +
//                     "    FOR UPDATE SKIP LOCKED";
//
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, walletId);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (!rs.next()) {
//                    return null;
//                }
//                return rs.getBigDecimal(1);
//            }
//        }
//    }
//
//    private void updateBalance(Connection conn, int walletId, BigDecimal newAmount) throws SQLException {
//        String sql = "    UPDATE wallet\n" +
//                     "    SET amount = ?,\n" +
////                     "        last_modified_at = ?\n" +
//                     "    WHERE wallet_id = ?\n";
//
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, walletId);
//            stmt.setString(2, "WITHDRAW");
//            stmt.setBigDecimal(3, newAmount);
//           // stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
//
//            stmt.execute();
//        }
//    }
//
//    @Override
//    public double getBalance(int id) {
//        String sql = "    SELECT amount\n" +
//                     "    FROM wallet\n" +
//                     "    WHERE wallet_id = ?\n" +
//                     "    FOR SHARE\n";
//
//        return jdbcTemplate.queryForObject(sql, BigDecimal.class, id).doubleValue();
//    }
//
//    @Override
//    public boolean findWalletId(int id) {
//        String sql = "SELECT EXISTS(SELECT 1 FROM wallet WHERE wallet_id = ?)";
//        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
//    }
//}


package org.example.storage.impl.dao;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.WalletZeroException;
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
        if ("WITHDRAW".equals(wallet.getOperationType())) {
            if (wallet.getAmount()>amount){throw new WalletZeroException("Balance 0.0");}
            else {  amount = amount - wallet.getAmount();
                jdbcTemplate.update("UPDATE wallet SET amount = ?, operation_type = ? WHERE wallet_id = ?", amount, wallet.getOperationType(), wallet.getId());
            }}
        return amount;
    }

    @Override
    public double getBalance(int id) {
        String sqlGetAmount =
                String.format("SELECT amount FROM wallet WHERE wallet_id = %d", id);
        return Double.parseDouble(String.valueOf(jdbcTemplate.queryForObject(sqlGetAmount, BigDecimal.class)));
    }

    @Override
    public boolean findWalletId(int id) {
        SqlRowSet walletRows = jdbcTemplate.queryForRowSet("SELECT * FROM wallet WHERE wallet_id = ?", id);
        return walletRows.next();
    }
}
