package com.extrawest.bdd_cpo_ocpi.repository;

import com.extrawest.ocpi.model.dto.VersionDto;
import com.extrawest.ocpi.model.enums.VersionNumber;

import java.util.List;

public interface VersionsRepository {
    void addAll(List<VersionDto> versions);

    String getVersionDetailsUrl(VersionNumber versionNumber);
    void clear();
}
