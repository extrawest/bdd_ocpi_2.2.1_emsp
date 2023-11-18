package com.extrawest.bdd_cpo_ocpi.validation.request;

import com.extrawest.bdd_cpo_ocpi.utils.Generators;
import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
import com.extrawest.ocpi.model.dto.Credentials;
import com.extrawest.ocpi.model.dto.CredentialsRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CredentialsRequestBddHandler extends OutgoingMessageFieldsFactory<Credentials>
        implements RequestMessageFactory<Credentials> {
    public static final String TOKEN_REQUIRED = "token";
    public static final String URL_REQUIRED = "url";
    public static final String ROLES_REQUIRED = "roles";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(TOKEN_REQUIRED, (req, token) -> req.setToken(getStringOrRandom(token, 34)));
        this.requiredFieldsSetup.put(URL_REQUIRED, (req, url) -> req.setUrl(getStringOrRandom(url, 34)));
        this.requiredFieldsSetup.put(ROLES_REQUIRED, (req, roles) -> req.setRoles(
                parseToListOrGenerateRandom(roles, ROLES_REQUIRED, CredentialsRole.class,
                        Generators::generateCredentialsRole)));


        this.optionalFieldsSetup = new HashMap<>();
    }

    @Override
    public Credentials createMessageWithValidatedParams(Map<String, String> params) {
        Credentials credentials = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + credentials);
        return credentials;
    }
}