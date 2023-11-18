package com.extrawest.bdd_cpo_ocpi.repository.impl;

import com.extrawest.bdd_cpo_ocpi.exception.BddTestingException;
import com.extrawest.bdd_cpo_ocpi.repository.VersionsRepository;
import com.extrawest.ocpi.model.dto.Version;
import com.extrawest.ocpi.model.enums.VersionNumber;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.extrawest.bdd_cpo_ocpi.exception.ApiErrorMessage.VERSION_2_2_1_NOT_SUPPORTED;

@Component
public class VersionsRepositoryImpl implements VersionsRepository {
    private final Map<VersionNumber, Version> versionsMap = new ConcurrentHashMap<>();

    @Override
    public void addAll(List<Version> versions) {
        Map<VersionNumber, Version> toMap = versions
                .stream()
                .collect(Collectors.toMap(Version::getVersion, (endpoint) -> endpoint));
        versionsMap.putAll(toMap);
    }

    @Override
    public void clear() {
        this.versionsMap.clear();
    }

    @Override
    public String getVersionDetailsUrl(VersionNumber versionNumber) {
        Version versionResponse = get(versionNumber);
        return versionResponse.getUrl();
    }

    private Version get(VersionNumber versionNumber) {
        if (!versionsMap.containsKey(VersionNumber.V_2_2_1)) {
            throw new BddTestingException(VERSION_2_2_1_NOT_SUPPORTED.getValue());
        }
        return versionsMap.get(versionNumber);
    }

}
