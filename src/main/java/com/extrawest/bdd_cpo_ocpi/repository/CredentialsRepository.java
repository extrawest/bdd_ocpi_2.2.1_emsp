package com.extrawest.bdd_cpo_ocpi.repository;

import com.extrawest.ocpi.model.dto.CredentialsDTO;

public interface CredentialsRepository {
    CredentialsDTO getEmspCredentials();
    void setEmspCredentials(CredentialsDTO emspCredentials);
}
