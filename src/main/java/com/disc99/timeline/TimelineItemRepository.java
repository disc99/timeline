package com.disc99.timeline;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TimelineItemRepository {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    List<TimelineItem> find(long lastId, @NonNull AccountId accountId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("lastId", lastId)
                .addValue("accountId", accountId.getValue());
        return jdbcTemplate.query("SELECT * FROM TIMELINE_ITEMS WHERE ID > :lastId AND ACCOUNT_ID = :accountId", param, new BeanPropertyRowMapper<>(TimelineItem.class));
    }

    List<TimelineItem> findAll(@NonNull AccountId accountId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("accountId", accountId.getValue());
        return jdbcTemplate.query("SELECT * FROM TIMELINE_ITEMS WHERE ACCOUNT_ID = :accountId", param, new BeanPropertyRowMapper<>(TimelineItem.class));
    }

    void save(@NonNull TimelineItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        jdbcTemplate.update("INSERT INTO TIMELINE_ITEMS (ACCOUNT_ID, SERVICE_ID, CONTENTS) VALUES(:accountId, :serviceId, :contents)", param);
    }
}
