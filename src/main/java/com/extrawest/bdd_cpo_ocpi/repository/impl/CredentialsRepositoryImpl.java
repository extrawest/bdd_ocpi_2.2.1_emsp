package com.extrawest.bdd_cpo_ocpi.repository.impl;

import com.extrawest.bdd_cpo_ocpi.repository.CredentialsRepository;
import com.extrawest.ocpi.model.dto.CredentialsDto;
import org.springframework.stereotype.Component;

@Component
public class CredentialsRepositoryImpl implements CredentialsRepository {
    private CredentialsDto emspCredentials;

    public CredentialsDto getEmspCredentials() {
        return this.emspCredentials;
    }

    public void setEmspCredentials(CredentialsDto emspCredentials) {
        this.emspCredentials = emspCredentials;
    }
}
