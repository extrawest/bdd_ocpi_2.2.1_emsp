package com.extrawest.bdd_cpo_ocpi;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;

import java.util.TimeZone;

@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
