package com.perkflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class PostgresConnectivityIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void connectsToPostgresAndExecutesSelectOne() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        assertEquals(1, result);
    }
}
