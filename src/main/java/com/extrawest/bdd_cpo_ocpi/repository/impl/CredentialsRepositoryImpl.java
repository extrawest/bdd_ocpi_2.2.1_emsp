package com.extrawest.bdd_cpo_ocpi.repository.impl;

import com.extrawest.bdd_cpo_ocpi.repository.CredentialsRepository;
import com.extrawest.ocpi.model.dto.CredentialsDTO;
import org.springframework.stereotype.Component;

@Component
public class CredentialsRepositoryImpl implements CredentialsRepository {
    private CredentialsDTO emspCredentials;

    public CredentialsDTO getEmspCredentials() {
        return this.emspCredentials;
    }

    public void setEmspCredentials(CredentialsDTO emspCredentials) {
        this.emspCredentials = emspCredentials;
    }
}
