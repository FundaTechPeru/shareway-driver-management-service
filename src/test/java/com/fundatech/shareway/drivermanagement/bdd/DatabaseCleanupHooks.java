package com.fundatech.shareway.drivermanagement.bdd;

import java.util.List;

import io.cucumber.java.Before;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseCleanupHooks {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleanupHooks(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Before(order = 0)
    public void truncateAllTables() {
        List<String> tables = jdbcTemplate.queryForList(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE'",
                String.class);
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        tables.forEach(table -> jdbcTemplate.execute("TRUNCATE TABLE " + table));
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
