package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.response.VersionResponseDTO;
import com.extrawest.ocpi.model.enums.VersionNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class VersionResponseBddHandler extends IncomingMessageFieldsFactory<VersionResponseDTO>
        implements ResponseMessageFactory<VersionResponseDTO> {
    public static final String VERSION_NUMBER_REQUIRED = "version";
    public static final String URL_REQUIRED = "url";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = Map.of(
                VERSION_NUMBER_REQUIRED, (req, versionNumber) -> {
                    if (nonEqual(wildCard, versionNumber)) {
                        req.setVersion(VersionNumber.fromValue(versionNumber));
                    }
                },
                URL_REQUIRED, (req, url) -> {
                    if (nonEqual(wildCard, url)) {
                        req.setUrl(url);
                    }
                }
        );

        this.assertionFactory = new HashMap<>();
        assertionFactory.put(VERSION_NUMBER_REQUIRED, (expectedParams, actual) -> compareVersionNumberIncludeWildCard(
                expectedParams, actual.getVersion(), VERSION_NUMBER_REQUIRED));
        assertionFactory.put(URL_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getUrl(), URL_REQUIRED));
    }


    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, VersionResponseDTO message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<VersionResponseDTO> getClazz() {
        return VersionResponseDTO.class;
    }
}
