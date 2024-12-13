package com.ifortex.bookservice.repo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {
    @Value("${spring.datasource.url}")
    protected String url ;
    @Value("${spring.datasource.username}")
    protected String username ;
    @Value("${spring.datasource.password}")
    protected String password ;

    public DBConfig() {
    }
}
