package com.extrawest.bdd_cpo_ocpi.repository;

import com.extrawest.ocpi.model.dto.CredentialsDto;

public interface CredentialsRepository {
    CredentialsDto getEmspCredentials();

    void setEmspCredentials(CredentialsDto emspCredentials);
}
