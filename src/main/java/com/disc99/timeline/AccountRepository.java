package com.disc99.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AccountRepository {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    Optional<Account> findByName(String name) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);
        return jdbcTemplate.query("SELECT * FROM ACCOUNTS WHERE NAME = :name", param, new AccountExtractor());
    }

    void save(Account account) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(account);
        jdbcTemplate.update("INSERT INTO ACCOUNTS(NAME, PASSWORD) VALUES(:name, :password)", param);
    }

    private class AccountExtractor implements ResultSetExtractor<Optional<Account>> {
        @Override
        public Optional<Account> extractData(ResultSet rs) throws SQLException, DataAccessException {
            rs.last();
            int row = rs.getRow();
            if (row > 1) {
                throw new IncorrectResultSizeDataAccessException(row);
            }
            if (row == 0) {
                return Optional.empty();
            }
            return Optional.of(Account.builder()
                    .id(new AccountId(rs.getLong("id")))
                    .name(rs.getString("name"))
                    .password(rs.getString("password"))
                    .build());
        }
    }
}
