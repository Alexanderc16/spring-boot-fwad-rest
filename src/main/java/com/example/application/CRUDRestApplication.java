package com.example.application;

import com.example.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example")
@SpringBootApplication(scanBasePackages = "com.example")
public class CRUDRestApplication implements CommandLineRunner {

  @Autowired
  private AppService service;

  public static void main(String[] args) {
    SpringApplication.run(CRUDRestApplication.class, args);
  }


  @Override
  public void run(String... args) {
    service.populate();
  }
}
