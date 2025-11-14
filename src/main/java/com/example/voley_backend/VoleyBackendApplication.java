package com.example.voley_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.voley")
@EnableJpaRepositories(basePackages = {"com.voley.adapter", "com.voley.repository"})
@EntityScan(basePackages = "com.voley.domain")
@EnableScheduling
public class VoleyBackendApplication {

public static void main(String[] args) {
SpringApplication.run(VoleyBackendApplication.class, args);
}

}
