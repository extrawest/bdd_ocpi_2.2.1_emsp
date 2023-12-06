package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.AuthorizationInfoDto;
import com.extrawest.ocpi.model.dto.DisplayText;
import com.extrawest.ocpi.model.dto.LocationReferencesDto;
import com.extrawest.ocpi.model.dto.token.TokenDto;
import com.extrawest.ocpi.model.enums.AllowedType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorizationInfoBddHandler extends IncomingMessageFieldsFactory<AuthorizationInfoDto>
        implements ResponseMessageFactory<AuthorizationInfoDto> {
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
                TokenDto parsedToken = parseModelFromJson(token, TOKEN_REQUIRED, TokenDto.class);
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
                LocationReferencesDto locationReferences = parseModelFromJson(location, LOCATION, LocationReferencesDto.class);
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
                expectedParams, actual.getToken(), TOKEN_REQUIRED, TokenDto.class));
        assertionFactory.put(AUTHORIZATION_REFERENCE, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getAuthorizationReference(), AUTHORIZATION_REFERENCE));
        assertionFactory.put(LOCATION, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getLocation(), LOCATION, LocationReferencesDto.class));
        assertionFactory.put(INFO, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getInfo(), INFO, DisplayText.class));
    }

    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, AuthorizationInfoDto message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<AuthorizationInfoDto> getClazz() {
        return AuthorizationInfoDto.class;
    }
}
