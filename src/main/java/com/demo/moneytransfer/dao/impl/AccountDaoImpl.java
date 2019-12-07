package com.demo.moneytransfer.dao.impl;

import com.demo.moneytransfer.dao.AccountDao;
import com.demo.moneytransfer.dao.DBHelper;
import com.demo.moneytransfer.domain.Account;
import com.demo.moneytransfer.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
                throw new AppException(500, -1, "Account cannot be created");
            }
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            } else {
                logger.error("Unable to create account, no ID obtained.");
                throw new AppException(500, -1, "Account Cannot be created");
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
