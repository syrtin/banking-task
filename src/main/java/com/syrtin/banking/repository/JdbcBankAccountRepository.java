package com.syrtin.banking.repository;

import com.syrtin.banking.domain.BankAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcBankAccountRepository implements BankAccountRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcBankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BankAccount> findAllByClientId(Long clientId) {
        return jdbcTemplate.query("SELECT * FROM bank_account WHERE client_id = ?", new Object[]{clientId}, new BankAccountRowMapper());
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO bank_account (account_number, account_currency, client_id) " +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, bankAccount.getNumber());
            ps.setString(2, bankAccount.getCurrency());
            ps.setLong(3, bankAccount.getClientId());
            return ps;
        }, keyHolder);
        bankAccount.setId(keyHolder.getKey().longValue());
        return bankAccount;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM bank_account WHERE id = ?", id);
    }

    private class BankAccountRowMapper implements RowMapper<BankAccount> {
        @Override
        public BankAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String number = rs.getString("account_number");
            String currency = rs.getString("account_currency");
            long clientId = rs.getLong("client_id");
            BankAccount bankAccount = new BankAccount();
            bankAccount.setId(id);
            bankAccount.setNumber(number);
            bankAccount.setCurrency(currency);
            bankAccount.setClientId(clientId);
            return bankAccount;
        }
    }
}