package com.example.voley_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.voley")
@EnableJpaRepositories(basePackages = "com.voley.adapter")
@EntityScan(basePackages = "com.voley.domain")
public class VoleyBackendApplication {

public static void main(String[] args) {
SpringApplication.run(VoleyBackendApplication.class, args);
}

}
