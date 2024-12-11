package com.ifortex.bookservice.repo;

import org.springframework.beans.factory.annotation.Value;

public class DBConfig {
    @Value("spring.datasource.url")
    private String url;
    @Value("spring.datasource.username")
    private String username;
    @Value("spring.datasource.password")
    private String password;

    public DBConfig() {
    }
}
