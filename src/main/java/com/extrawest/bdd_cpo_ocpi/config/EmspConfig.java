package com.extrawest.bdd_cpo_ocpi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class EmspConfig {
    @Value("${emsp.version.url}")
    private String versionUrl;
    @Value("${emsp.token.a}")
    private String tokenA;
}
