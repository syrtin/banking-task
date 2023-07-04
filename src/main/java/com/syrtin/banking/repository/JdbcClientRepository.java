package com.syrtin.banking.repository;

import com.syrtin.banking.domain.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
public class JdbcClientRepository implements ClientRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Client> findAll() {
        return jdbcTemplate.query("SELECT * FROM client", new ClientRowMapper());
    }

    @Override
    public Client save(Client client) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO client (lastname, firstname, middlename, document_type, document_sn, birth_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, client.getLastname());
            ps.setString(2, client.getFirstname());
            ps.setString(3, client.getMiddlename());
            ps.setString(4, client.getDocumentType());
            ps.setString(5, client.getDocumentSN());
            ps.setDate(6, Date.valueOf(client.getBirthDate()));
            return ps;
        }, keyHolder);
        client.setId(keyHolder.getKey().longValue());
        return client;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM client WHERE id = ?", id);
    }

    private class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String lastName = rs.getString("lastname");
            String firstName = rs.getString("firstname");
            String middleName = rs.getString("middlename");
            String documentType =  rs.getString("document_type");
            String documentSn = rs.getString("document_sn");
            LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
            Client client = new Client();
            client.setId(id);
            client.setLastname(lastName);
            client.setFirstname(firstName);
            client.setMiddlename(middleName);
            client.setDocumentType(documentType);
            client.setDocumentSN(documentSn);
            client.setBirthDate(birthDate);
            return client;
        }
    }
}

