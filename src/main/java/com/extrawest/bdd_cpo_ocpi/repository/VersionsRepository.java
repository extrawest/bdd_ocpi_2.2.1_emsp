package com.extrawest.bdd_cpo_ocpi.repository;

import com.extrawest.ocpi.model.dto.response.VersionResponseDTO;
import com.extrawest.ocpi.model.enums.VersionNumber;

import java.util.List;

public interface VersionsRepository {
    void addAll(List<VersionResponseDTO> versions);
    String getVersionDetailsUrl(VersionNumber versionNumber);
    void clear();
}
