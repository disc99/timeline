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

    List<TimelineItem> find(long lastId, @NonNull String userId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("lastId", lastId)
                .addValue("userId", userId);
        return jdbcTemplate.query("SELECT * FROM TIMELINE_ITEMS WHERE ID > :lastId AND USER_ID = :userId", param, new BeanPropertyRowMapper<>(TimelineItem.class));
    }

    List<TimelineItem> findAll(@NonNull String userId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId);
        return jdbcTemplate.query("SELECT * FROM TIMELINE_ITEMS WHERE USER_ID = :userId", param, new BeanPropertyRowMapper<>(TimelineItem.class));
    }

    void save(@NonNull TimelineItem item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        jdbcTemplate.update("INSERT INTO TIMELINE_ITEMS (USER_ID, SERVICE_ID, CONTENTS) VALUES(:userId, :serviceId, :contents)", param);
    }
}
