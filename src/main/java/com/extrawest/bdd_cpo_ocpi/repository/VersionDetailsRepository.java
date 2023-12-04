package com.extrawest.bdd_cpo_ocpi.repository;

import com.extrawest.ocpi.model.dto.Endpoint;
import com.extrawest.ocpi.model.dto.VersionDetailsDto;
import com.extrawest.ocpi.model.enums.ModuleID;

public interface VersionDetailsRepository {
    void addAll(VersionDetailsDto details);

    Endpoint getEndpoint(ModuleID moduleID);
    void clear();

}
