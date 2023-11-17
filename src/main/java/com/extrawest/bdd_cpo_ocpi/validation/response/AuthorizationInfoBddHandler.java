package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.TokenDTO;
import com.extrawest.ocpi.model.dto.request.LocationReferences;
import com.extrawest.ocpi.model.dto.response.AuthorizationInfo;
import com.extrawest.ocpi.model.enums.AllowedType;
import com.extrawest.ocpi.model.vo.DisplayText;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorizationInfoBddHandler extends IncomingMessageFieldsFactory<AuthorizationInfo>
        implements ResponseMessageFactory<AuthorizationInfo> {
    public static final String ALLOWED_REQUIRED = "allowed";
    public static final String TOKEN_REQUIRED = "token";

    public static final String LOCATION = "location";
    public static final String AUTHORIZATION_REFERENCE = "authorization_reference";
    public static final String INFO = "info";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(ALLOWED_REQUIRED, (req, allowed) -> {
            if (nonEqual(wildCard, allowed)) {
                req.setAllowed(AllowedType.valueOf(allowed));
            }
        });
        this.requiredFieldsSetup.put(TOKEN_REQUIRED, (req, token) -> {
            if (nonEqual(wildCard, token)) {
                TokenDTO parsedToken = parseModelFromJson(token, TOKEN_REQUIRED, TokenDTO.class);
                req.setToken(parsedToken);
            }
        });

        this.optionalFieldsSetup = new HashMap<>();
        this.optionalFieldsSetup.put(AUTHORIZATION_REFERENCE, (req, authRef) -> {
            if (nonEqual(wildCard, authRef)) {
                req.setAuthorizationReference(authRef);
            }
        });
        this.optionalFieldsSetup.put(LOCATION, (req, location) -> {
            if (nonEqual(wildCard, location)) {
                LocationReferences locationReferences = parseModelFromJson(location, LOCATION, LocationReferences.class);
                req.setLocation(locationReferences);
            }
        });
        this.optionalFieldsSetup.put(INFO, (req, info) -> {
            if (nonEqual(wildCard, info)) {
                DisplayText displayText = parseModelFromJson(info, INFO, DisplayText.class);
                req.setInfo(displayText);
            }
        });

        this.assertionFactory = new HashMap<>();
        assertionFactory.put(ALLOWED_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getAllowed().value(), ALLOWED_REQUIRED));
        assertionFactory.put(TOKEN_REQUIRED, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getToken(), TOKEN_REQUIRED, TokenDTO.class));
        assertionFactory.put(AUTHORIZATION_REFERENCE, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getAuthorizationReference(), AUTHORIZATION_REFERENCE));
        assertionFactory.put(LOCATION, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getLocation(), LOCATION, LocationReferences.class));
        assertionFactory.put(INFO, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getInfo(), INFO, DisplayText.class));
    }

    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, AuthorizationInfo message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<AuthorizationInfo> getClazz() {
        return AuthorizationInfo.class;
    }
}
