package com.joven.poller.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://database-1.c1do0euvzerj.ap-southeast-1.rds.amazonaws.com/poll_and_vote")
                .username("root")
                .password("PJf0r3v3r!")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
