package com.extrawest.bdd_cpo_ocpi.repository;

import com.extrawest.ocpi.model.dto.Endpoint;
import com.extrawest.ocpi.model.dto.VersionDetails;
import com.extrawest.ocpi.model.enums.ModuleID;

public interface VersionDetailsRepository {
    void addAll(VersionDetails details);

    Endpoint getEndpoint(ModuleID moduleID);
    void clear();

}
