package com.extrawest.bdd_cpo_ocpi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
public class AppConfig {
    @Bean
    public ObjectMapper mapper() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        return JsonMapper.builder()
                .findAndAddModules()
                .defaultDateFormat(df)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
    }
}
