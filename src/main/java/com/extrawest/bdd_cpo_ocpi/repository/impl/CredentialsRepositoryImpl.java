package com.extrawest.bdd_cpo_ocpi.repository.impl;

import com.extrawest.bdd_cpo_ocpi.repository.CredentialsRepository;
import com.extrawest.ocpi.model.dto.Credentials;
import org.springframework.stereotype.Component;

@Component
public class CredentialsRepositoryImpl implements CredentialsRepository {
    private Credentials emspCredentials;

    public Credentials getEmspCredentials() {
        return this.emspCredentials;
    }

    public void setEmspCredentials(Credentials emspCredentials) {
        this.emspCredentials = emspCredentials;
    }
}
