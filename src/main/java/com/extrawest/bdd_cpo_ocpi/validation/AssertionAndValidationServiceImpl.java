package com.extrawest.bdd_cpo_ocpi.validation;

import com.extrawest.bdd_cpo_ocpi.exception.BddTestingException;
import com.extrawest.bdd_cpo_ocpi.models.enums.ImplementedMessageType;
import com.extrawest.bdd_cpo_ocpi.validation.request.*;
import com.extrawest.bdd_cpo_ocpi.validation.response.*;
import com.extrawest.ocpi.model.dto.*;
import com.extrawest.ocpi.model.dto.location.Connector;
import com.extrawest.ocpi.model.dto.location.Location;
import com.extrawest.ocpi.model.dto.tariff.TariffDto;
import com.extrawest.ocpi.model.markers.OcpiRequestData;
import com.extrawest.ocpi.model.markers.OcpiResponseData;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.extrawest.bdd_cpo_ocpi.exception.ApiErrorMessage.*;
import static com.extrawest.bdd_cpo_ocpi.models.enums.ImplementedMessageType.*;

@Service
@RequiredArgsConstructor
public class AssertionAndValidationServiceImpl implements AssertionAndValidationService {
    private final TariffRequestBddHandler tariffRequestBddHandler;
    private final TariffResponseBddHandler tariffResponseBddHandler;
    private final VersionDetailsResponseBddHandler versionDetailsResponseBddHandler;
    private final VersionResponseBddHandler versionResponseBddHandler;
    private final SessionRequestBddHandler sessionRequestBddHandler;
    private final SessionResponseBddHandler sessionResponseBddHandler;

    private final CdrRequestBddHandler cdrRequestBddHandler;
    private final CdrResponseBddHandler cdrResponseBddHandler;

    private final LocationRequestBddHandler locationRequestBddHandler;
    private final EvseRequestBddHandler evseRequestBddHandler;
    private final ConnectorRequestBddHandler connectorRequestBddHandler;
    private final LocationResponseBddHandler locationResponseBddHandler;
    private final EvseResponseBddHandler evseResponseBddHandler;
    private final ConnectorResponseBddHandler connectorResponseBddHandler;

    private final TokenRequestBddHandler tokenRequestBddHandler;

    private final LocationReferencesBddHandler locationReferencesBddHandler;
    private final AuthorizationInfoBddHandler authorizationInfoBddHandler;

    private final CommandResultBddHandler commandResultBddHandler;

    private final CredentialsRequestBddHandler credentialsRequestBddHandler;
    private final CredentialsResponseBddHandler credentialsResponseBddHandler;

    Map<ImplementedMessageType, RequestMessageFactory<? extends OcpiRequestData>> requestHandlers;
    Map<ImplementedMessageType, ResponseMessageFactory<? extends OcpiResponseData>> responseHandlers;

    @PostConstruct
    public void init() {
        createOutgoingRequestHandlers();
        createIncomingRequestHandlers();
        validateForFactoriesForImplementedMessageTypes();
    }

    private void createOutgoingRequestHandlers() {
        requestHandlers = Map.of(TARIFF, tariffRequestBddHandler,
                SESSION, sessionRequestBddHandler,
                CDR, cdrRequestBddHandler,
                LOCATION, locationRequestBddHandler,
                EVSE, evseRequestBddHandler,
                CONNECTOR, connectorRequestBddHandler,
                TOKENS, tokenRequestBddHandler,
                AUTHORIZE, locationReferencesBddHandler,
                COMMAND, commandResultBddHandler,
                CREDENTIALS, credentialsRequestBddHandler);
    }

    private void createIncomingRequestHandlers() {
        responseHandlers = Map.of(
                TARIFF, tariffResponseBddHandler,
                VERSION_DETAILS, versionDetailsResponseBddHandler,
                VERSION, versionResponseBddHandler,
                SESSION, sessionResponseBddHandler,
                CDR, cdrResponseBddHandler,
                LOCATION, locationResponseBddHandler,
                EVSE, evseResponseBddHandler,
                CONNECTOR, connectorResponseBddHandler,
                AUTHORIZATION_INFO, authorizationInfoBddHandler,
                CREDENTIALS, credentialsResponseBddHandler);
    }

    @Override
    public RequestMessageFactory<? extends OcpiRequestData> getOutgoingRequestFactory(ImplementedMessageType type) {
        RequestMessageFactory<? extends OcpiRequestData> factory = requestHandlers
                .getOrDefault(type, null);
        if (Objects.isNull(factory)) {
            throw new BddTestingException(String.format(INVALID_OUTGOING_FACTORY.getValue(), type.getValue()));
        }
        return factory;
    }

    @Override
    public ResponseMessageFactory<? extends OcpiResponseData> getIncomingResponseFactory(ImplementedMessageType type) {
        ResponseMessageFactory<? extends OcpiResponseData> factory = responseHandlers
                .getOrDefault(type, null);
        if (Objects.isNull(factory)) {
            throw new BddTestingException(String.format(INVALID_INCOMING_FACTORY.getValue(), type.getValue()));
        }
        return factory;
    }

    private void validateForFactoriesForImplementedMessageTypes() {
        Arrays.stream(ImplementedMessageType.values()).forEach(type -> {
            if (!requestHandlers.containsKey(type) && !responseHandlers.containsKey(type)) {
                throw new BddTestingException(String.format(INVALID_OUTGOING_FACTORY.getValue(), type.getValue()));
            }
        });
    }

    private <T> void validateResponseWithLibModel(T object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (violations.size() != 0) {
            List<String> msgs = new ArrayList<>();
            for (ConstraintViolation<T> violation : violations) {
                msgs.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
            throw new BddTestingException(String.format(NOT_MATCHING_MODEL_CONSTRAINT.getValue(), msgs));
        }
    }


    @Override
    public ImplementedMessageType validateAndAssertFields(OcpiResponseData responseData,
                                                          Map<String, String> parameters) {
        validateResponseWithLibModel(responseData);
        if (responseData instanceof TariffDto response) {
            tariffResponseBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return TARIFF;
        }
        if (responseData instanceof VersionDetailsDto response) {
            versionDetailsResponseBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return VERSION_DETAILS;
        }
        if (responseData instanceof VersionDto response) {
            versionResponseBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return VERSION;
        }
        if (responseData instanceof SessionDto response) {
            sessionResponseBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return SESSION;
        }
        if (responseData instanceof com.extrawest.ocpi.model.dto.cdr.CDRDto response) {
            cdrResponseBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return CDR;
        }
        if (responseData instanceof Location response) {
            locationResponseBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return LOCATION;
        }
        if (responseData instanceof com.extrawest.ocpi.model.dto.location.EVSE response) {
            evseResponseBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return EVSE;
        }
        if (responseData instanceof Connector response) {
            connectorResponseBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return CONNECTOR;
        }
        if (responseData instanceof AuthorizationInfoDto response) {
            authorizationInfoBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return AUTHORIZATION_INFO;
        }
        if (responseData instanceof CredentialsDto response) {
            credentialsResponseBddHandler.validateAndAssertFieldsWithParams(parameters, response);
            return CREDENTIALS;
        }
        else {
            throw new BddTestingException(INVALID_RESPONSE_TYPE.getValue() + responseData);
        }
    }
}
