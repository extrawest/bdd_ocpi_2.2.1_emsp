package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.CredentialsDTO;
import com.extrawest.ocpi.model.vo.CredentialsRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CredentialsResponseBddHandler extends IncomingMessageFieldsFactory<CredentialsDTO>
        implements ResponseMessageFactory<CredentialsDTO> {
    public static final String TOKEN_REQUIRED = "token";
    public static final String URL_REQUIRED = "url";
    public static final String ROLES_REQUIRED = "roles";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(TOKEN_REQUIRED, (req, token) -> {
            if (nonEqual(wildCard, token)) {
                req.setToken(token);
            }
        });
        this.requiredFieldsSetup.put(URL_REQUIRED, (req, url) -> {
            if (nonEqual(wildCard, url)) {
                req.setUrl(url);
            }
        });
        this.requiredFieldsSetup.put(ROLES_REQUIRED, (req, roles) -> {
            if (nonEqual(wildCard, roles)) {
                CredentialsRole[] credentialsRoles = parseModelsFromJson(roles, ROLES_REQUIRED, CredentialsRole.class);
                req.setRoles(List.of(credentialsRoles));
            }
        });

        this.optionalFieldsSetup = new HashMap<>();

        this.assertionFactory = new HashMap<>();
        assertionFactory.put(TOKEN_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getToken(), TOKEN_REQUIRED));
        assertionFactory.put(URL_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getUrl(), URL_REQUIRED));
        assertionFactory.put(ROLES_REQUIRED, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getRoles(), ROLES_REQUIRED, CredentialsRole.class));
    }

    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, CredentialsDTO message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<CredentialsDTO> getClazz() {
        return CredentialsDTO.class;
    }
}
