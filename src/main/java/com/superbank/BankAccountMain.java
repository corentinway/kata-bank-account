package com.superbank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.superbank")
@EntityScan(basePackages = "com.superbank")
public class BankAccountMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountMain.class);

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(BankAccountMain.class);

        // Ajouter un écouteur pour loguer le port
        app.addListeners((ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
            String port = event.getEnvironment().getProperty("server.port", "8080"); // Par défaut : 8080
            LOGGER.info("L'application s'exécute sur le port : " + port);
        });

        app.run(args);
    }
}