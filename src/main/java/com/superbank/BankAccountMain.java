package com.superbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.superbank")
@EntityScan(basePackages = "com.superbank")
public class BankAccountMain {
    public static void main(String[] args) {
        SpringApplication.run(BankAccountMain.class, args);
    }
}