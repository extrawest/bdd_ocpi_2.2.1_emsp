package com.extrawest.bdd_cpo_ocpi.repository;

import com.extrawest.ocpi.model.dto.Credentials;

public interface CredentialsRepository {
    Credentials getEmspCredentials();

    void setEmspCredentials(Credentials emspCredentials);
}
