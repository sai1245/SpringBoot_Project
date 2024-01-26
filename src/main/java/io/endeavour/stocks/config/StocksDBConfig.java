package io.endeavour.stocks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class StocksDBConfig {
    @Autowired
    DataSource dataSource;

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(dataSource);
    }

    @Bean(name="namedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate getNamedParamJdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
