package com.demo.moneytransfer.dao.impl;

import com.demo.moneytransfer.dao.AccountDao;
import com.demo.moneytransfer.dao.DBHelper;
import com.demo.moneytransfer.domain.Account;
import com.demo.moneytransfer.domain.RequestType;
import com.demo.moneytransfer.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

    private static Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

    @Inject
    private DBHelper dbHelper;

    @Override
    public Long create(Account account) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet keys = null;
        try {
            conn = dbHelper.getConnection();
            stmt = conn.prepareStatement("INSERT INTO account (username, balance) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, account.getUsername());
            stmt.setBigDecimal(2, account.getBalance());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                logger.error("Unable to create account, no rows affected.");
                throw new AppException(500, -1, "Account could not be created");
            }
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            } else {
                logger.error("Unable to create account, no ID obtained.");
                throw new AppException(500, -1, "Account could not be created");
            }
        } catch (SQLException e) {
            logger.error("Error Inserting Account ", e);
            if(e.getLocalizedMessage().contains("Unique index or primary key violation") || e.getLocalizedMessage().contains("Duplicate entry")) {
                throw new  AppException(400, 6, "username already exist");
            } else {
                throw new  AppException(500, -1, "Unable to create account");
            }
        } finally {
            close(conn, stmt, keys);
        }
    }

    @Override
    public void delete(String username) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbHelper.getConnection();
            stmt = conn.prepareStatement("DELETE FROM account WHERE username = ?");
            stmt.setString(1, username);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                logger.error("Unable to delete account, no rows affected.");
                throw new AppException(500, -1, "Account could not be deleted");
            }
        } catch (SQLException e) {
            logger.error("Error deleting account ", e);
            throw new AppException(500, -1, "Account could not be deleted");
        } finally {
            close(conn, stmt, null);
        }
    }

    @Override
    public List<Account> findAll() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Account> accounts = new ArrayList<>();
        try {
            conn = dbHelper.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM account");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Account acc = new Account(rs.getLong("id"), rs.getString("username"),
                        rs.getBigDecimal("balance"));
                accounts.add(acc);
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AppException(500, -1, "Error fetching accounts");
        } finally {
            close(conn, stmt, rs);
        }
    }

    @Override
    public Optional<Account> findByUsername(String username) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Account account = null;
        try {
            conn = dbHelper.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM account WHERE username = ? ");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                account = new Account(rs.getLong("id"), rs.getString("username"), rs.getBigDecimal("balance"));
                logger.debug("Retrieve Account By Id: " + account);
                return Optional.of(account);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AppException(500, -1, "Error fetching account with username - " + username);
        } finally {
            close(conn, stmt, rs);
        }
    }

    @Override
    public int updateBalance(String username, BigDecimal amount, RequestType type) throws Exception {
        Connection conn = null;
        PreparedStatement lockStmt = null;
        PreparedStatement updateStmt = null;


        ResultSet rs = null;
        Account account = null;
        int updateCount = -1;
        try {
            conn = dbHelper.getConnection();
            conn.setAutoCommit(false);

            lockStmt = conn.prepareStatement("SELECT * FROM account WHERE username = ? FOR UPDATE");
            lockStmt.setString(1, username);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                account = new Account(rs.getLong("id"), rs.getString("username"),
                        rs.getBigDecimal("balance"));
                logger.debug("updateAccountBalance from Account: {}", account);
            }

            if (account == null) {
                throw new AppException(404, 4, "Invalid account - " + username);
            }

            BigDecimal newBalance = null;
            if (type == RequestType.CREDIT) {
                newBalance = account.getBalance().add(amount);
            } else if (type == RequestType.DEBIT) {
                newBalance = account.getBalance().subtract(amount);
                if (newBalance.compareTo(new BigDecimal(0)) < 0) {
                    throw new AppException(400, 5, "Insufficient balance");
                }
            }

            updateStmt = conn.prepareStatement("UPDATE account SET balance = ? WHERE username = ? ");
            updateStmt.setBigDecimal(1, newBalance);
            updateStmt.setString(2, username);
            updateCount = updateStmt.executeUpdate();
            conn.commit();

            return updateCount;
        } catch (SQLException e) {
            logger.error("Transaction Failed, rollback initiated for: " + username, e);
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ee) {
                throw new AppException(500, 5, "Fail to rollback transaction");
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (lockStmt != null) {
                    lockStmt.close();
                }
                if (updateStmt != null) {
                    updateStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return updateCount;
    }

    @Override
    public int transferMoney(String srcUsername, String destUsername, BigDecimal amount) throws Exception {
        int result = -1;
        Connection conn = null;
        PreparedStatement lockStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        Account srcAccount = null;
        Account destAccount = null;

        try {
            conn = dbHelper.getConnection();
            conn.setAutoCommit(false);

            lockStmt = conn.prepareStatement("SELECT * FROM account WHERE username = ? FOR UPDATE");
            lockStmt.setString(1, srcUsername);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                srcAccount = new Account(rs.getLong("id"), rs.getString("username"),
                        rs.getBigDecimal("balance"));
            }
            lockStmt = conn.prepareStatement("SELECT * FROM account WHERE username = ? FOR UPDATE");
            lockStmt.setString(1, destUsername);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                destAccount = new Account(rs.getLong("id"), rs.getString("username"), rs.getBigDecimal("balance"));
            }

            if (srcAccount == null) {
                throw new AppException(404, 4, "Invalid source account - " + srcUsername);
            } else if(destAccount == null) {
                throw new AppException(404, 4, "Invalid destination account - " + destUsername);
            }

            BigDecimal newSrcBalance = srcAccount.getBalance().subtract(amount);
            if (newSrcBalance.compareTo(new BigDecimal(0)) < 0) {
                throw new AppException(400, 5, "Insufficient balance");
            }

            updateStmt = conn.prepareStatement("UPDATE account SET balance = ? WHERE username = ?");
            updateStmt.setBigDecimal(1, newSrcBalance);
            updateStmt.setString(2, srcUsername);
            updateStmt.addBatch();
            updateStmt.setBigDecimal(1, destAccount.getBalance().add(amount));
            updateStmt.setString(2, destUsername);
            updateStmt.addBatch();
            int[] rowsUpdated = updateStmt.executeBatch();

            result = rowsUpdated[0] + rowsUpdated[1];

            conn.commit();
        } catch (SQLException e) {
            logger.error("User Transaction Failed, rollback initiated for: ", e);
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ee) {
                throw new Exception("Fail to rollback transaction", ee);
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (lockStmt != null) {
                    lockStmt.close();
                }
                if (updateStmt != null) {
                    updateStmt.close();
                }

            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return result;
    }

    private void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
