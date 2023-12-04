package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.Endpoint;
import com.extrawest.ocpi.model.dto.VersionDetailsDto;
import com.extrawest.ocpi.model.enums.VersionNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class VersionDetailsResponseBddHandler extends IncomingMessageFieldsFactory<VersionDetailsDto>
        implements ResponseMessageFactory<VersionDetailsDto> {
    public static final String VERSION_NUMBER_REQUIRED = "version";
    public static final String ENDPOINTS_REQUIRED = "endpoints";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = Map.of(
                VERSION_NUMBER_REQUIRED, (req, versionNumber) -> {
                    if (nonEqual(wildCard, versionNumber)) {
                        req.setVersion(VersionNumber.fromValue(versionNumber));
                    }
                },
                ENDPOINTS_REQUIRED, (req, endpoints) -> {
                    if (nonEqual(wildCard, endpoints)) {
                        Endpoint[] endpointsList = parseModelsFromJson(endpoints, ENDPOINTS_REQUIRED, Endpoint.class);
                        req.setEndpoints(Arrays.asList(endpointsList));
                    }
                }
        );

        this.assertionFactory = new HashMap<>();
        assertionFactory.put(VERSION_NUMBER_REQUIRED, (expectedParams, actual) -> compareVersionNumberIncludeWildCard(
                expectedParams, actual.getVersion(), VERSION_NUMBER_REQUIRED));
        assertionFactory.put(ENDPOINTS_REQUIRED, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getEndpoints(), ENDPOINTS_REQUIRED, Endpoint.class));
    }


    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, VersionDetailsDto message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<VersionDetailsDto> getClazz() {
        return VersionDetailsDto.class;
    }
}
