package com.extrawest.bdd_cpo_ocpi.repository;

import com.extrawest.ocpi.model.dto.Version;
import com.extrawest.ocpi.model.enums.VersionNumber;

import java.util.List;

public interface VersionsRepository {
    void addAll(List<Version> versions);

    String getVersionDetailsUrl(VersionNumber versionNumber);
    void clear();
}
