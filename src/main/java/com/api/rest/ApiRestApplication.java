package com.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EntityScan(basePackages = {"com.api.rest.model"})
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.api.rest.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@SpringBootApplication
@EnableCaching
public class ApiRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestApplication.class, args);
	}

}
