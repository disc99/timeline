package com.disc99.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepository {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    Account findByName(String name) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);
        return jdbcTemplate.queryForObject("SELECT * FROM ACCOUNTS WHERE NAME = :name", param, new BeanPropertyRowMapper<>(Account.class));
    }

    void save(Account account) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(account);
        jdbcTemplate.update("INSERT INTO ACCOUNTS(NAME, PASSWORD) VALUES(:name, :password)", param);
    }
}
