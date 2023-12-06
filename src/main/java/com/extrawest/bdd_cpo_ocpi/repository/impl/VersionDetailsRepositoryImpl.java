package com.extrawest.bdd_cpo_ocpi.repository.impl;

import com.extrawest.bdd_cpo_ocpi.exception.BddTestingException;
import com.extrawest.bdd_cpo_ocpi.repository.VersionDetailsRepository;
import com.extrawest.ocpi.model.dto.Endpoint;
import com.extrawest.ocpi.model.dto.VersionDetailsDto;
import com.extrawest.ocpi.model.enums.ModuleID;
import com.extrawest.ocpi.model.enums.VersionNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.extrawest.bdd_cpo_ocpi.exception.ApiErrorMessage.*;

@Component
@RequiredArgsConstructor
public class VersionDetailsRepositoryImpl implements VersionDetailsRepository {
    private final List<Endpoint> endpoints = new ArrayList<>();

    @Override
    public void addAll(VersionDetailsDto details) {
        if (!details.getVersion().equals(VersionNumber.V_2_2_1)) {
            throw new BddTestingException(String.format(VERSION_IS_NOT_2_2_1.getValue(), details.getVersion()));
        }
        endpoints.addAll(details.getEndpoints());
    }

    @Override
    public void clear() {
        this.endpoints.clear();
    }

    @Override
    public Endpoint getEndpoint(ModuleID moduleID) {
        if (CollectionUtils.isEmpty(endpoints)) {
            throw new BddTestingException(String.format(CPO_NOT_REGISTERED.getValue()));
        }
        return endpoints.stream().filter(e -> e.getIdentifier().equals(moduleID)).findFirst()
                .orElseThrow(() -> new BddTestingException(String.format(MODULE_NOT_IMPLEMENTED.getValue(),
                        moduleID, endpoints.stream().map(Endpoint::getIdentifier))));
    }
}
