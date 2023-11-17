package com.extrawest.bdd_cpo_ocpi.repository;

import com.extrawest.ocpi.model.dto.response.VersionDetailsResponseDTO;
import com.extrawest.ocpi.model.enums.ModuleID;
import com.extrawest.ocpi.model.vo.Endpoint;

public interface VersionDetailsRepository {
    void addAll(VersionDetailsResponseDTO details);
    Endpoint getEndpoint(ModuleID moduleID);
    void clear();

}
