package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.ChargingPeriod;
import com.extrawest.ocpi.model.dto.Price;
import com.extrawest.ocpi.model.dto.SessionDto;
import com.extrawest.ocpi.model.dto.cdr.CdrToken;
import com.extrawest.ocpi.model.enums.AuthMethod;
import com.extrawest.ocpi.model.enums.SessionStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionResponseBddHandler extends IncomingMessageFieldsFactory<SessionDto>
        implements ResponseMessageFactory<SessionDto> {
    public static final String COUNTRY_CODE_REQUIRED = "country_code";
    public static final String PARTY_ID_REQUIRED = "party_id";
    public static final String ID_REQUIRED = "id";
    public static final String CURRENCY_REQUIRED = "currency";
    public static final String LAST_UPDATED_REQUIRED = "last_updated";
    public static final String START_DATE_TIME_REQUIRED = "start_date_time";
    public static final String KWH_REQUIRED = "kwh";
    public static final String CDR_TOKEN_REQUIRED = "cdr_token";
    public static final String AUTH_METHOD_REQUIRED = "auth_method";
    public static final String LOCATION_ID_REQUIRED = "location_id";
    public static final String EVSE_UID_REQUIRED = "evse_uid";
    public static final String CONNECTOR_ID_REQUIRED = "connector_id";
    public static final String STATUS_REQUIRED = "status";

    public static final String END_DATE_TIME = "end_date_time";
    public static final String AUTHORIZATION_REFERENCE = "authorization_reference";
    public static final String METER_ID = "meter_id";
    public static final String CHARGING_PERIODS = "charging_periods";
    public static final String TOTAL_COST = "totalCost";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(COUNTRY_CODE_REQUIRED, (req, model) -> {
            if (nonEqual(wildCard, model)) {
                req.setCountryCode(model);
            }
        });
        this.requiredFieldsSetup.put(PARTY_ID_REQUIRED, (req, partyId) -> {
            if (nonEqual(wildCard, partyId)) {
                req.setPartyId(partyId);
            }
        });
        this.requiredFieldsSetup.put(ID_REQUIRED, (req, id) -> {
            if (nonEqual(wildCard, id)) {
                req.setId(id);
            }
        });
        this.requiredFieldsSetup.put(CURRENCY_REQUIRED, (req, currency) -> {
            if (nonEqual(wildCard, currency)) {
                req.setCurrency(currency);
            }
        });
        this.requiredFieldsSetup.put(LAST_UPDATED_REQUIRED, (req, lastUpdated) -> {
            if (nonEqual(wildCard, lastUpdated)) {
                req.setLastUpdated(LocalDateTime.parse(lastUpdated));
            }
        });
        this.requiredFieldsSetup.put(START_DATE_TIME_REQUIRED, (req, startDateTime) -> {
            if (nonEqual(wildCard, startDateTime)) {
                req.setStartDateTime(LocalDateTime.parse(startDateTime));
            }
        });
        this.requiredFieldsSetup.put(KWH_REQUIRED, (req, kwh) -> {
            if (nonEqual(wildCard, kwh)) {
                req.setKwh(Float.parseFloat(kwh));
            }
        });
        this.requiredFieldsSetup.put(CDR_TOKEN_REQUIRED, (req, cdrToken) -> {
            if (nonEqual(wildCard, cdrToken)) {
                CdrToken cdrTokenObj = parseModelFromJson(cdrToken, CDR_TOKEN_REQUIRED, CdrToken.class);
                req.setCdrToken(cdrTokenObj);
            }
        });
        this.requiredFieldsSetup.put(AUTH_METHOD_REQUIRED, (req, authMethod) -> {
            if (nonEqual(wildCard, authMethod)) {
                req.setAuthMethod(AuthMethod.valueOf(authMethod));
            }
        });
        this.requiredFieldsSetup.put(LOCATION_ID_REQUIRED, (req, locationId) -> {
            if (nonEqual(wildCard, locationId)) {
                req.setLocationId(locationId);
            }
        });
        this.requiredFieldsSetup.put(EVSE_UID_REQUIRED, (req, evseUid) -> {
            if (nonEqual(wildCard, evseUid)) {
                req.setLocationId(evseUid);
            }
        });
        this.requiredFieldsSetup.put(CONNECTOR_ID_REQUIRED, (req, connectorId) -> {
            if (nonEqual(wildCard, connectorId)) {
                req.setConnectorId(connectorId);
            }
        });
        this.requiredFieldsSetup.put(STATUS_REQUIRED, (req, sessionStatus) -> {
            if (nonEqual(wildCard, sessionStatus)) {
                req.setStatus(SessionStatus.valueOf(sessionStatus));
            }
        });

        this.optionalFieldsSetup = Map.of(
                AUTHORIZATION_REFERENCE, (req, authRef) -> {
                    if (nonEqual(wildCard, authRef)) {
                        req.setAuthorizationReference(authRef);
                    }
                },
                METER_ID, (req, meterId) -> {
                    if (nonEqual(wildCard, meterId)) {
                        req.setMeterId(meterId);
                    }
                },
                CHARGING_PERIODS, (req, chargingPeriods) -> {
                    if (nonEqual(wildCard, chargingPeriods)) {
                        ChargingPeriod[] chargingPeriod =

                                parseModelsFromJson(chargingPeriods, CHARGING_PERIODS, ChargingPeriod.class);
                        req.setChargingPeriods(List.of(chargingPeriod));
                    }
                },
                TOTAL_COST, (req, totalCost) -> {
                    if (nonEqual(wildCard, totalCost)) {
                        req.setTotalCost(parseModelFromJson(totalCost, TOTAL_COST, Price.class));
                    }
                },
                END_DATE_TIME, (req, endDateTime) -> {
                    if (nonEqual(wildCard, endDateTime)) {
                        req.setEndDateTime(LocalDateTime.parse(endDateTime));
                    }
                }
        );

        this.assertionFactory = new HashMap<>();
        assertionFactory.put(COUNTRY_CODE_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getCountryCode(), COUNTRY_CODE_REQUIRED));
        assertionFactory.put(PARTY_ID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getPartyId(), PARTY_ID_REQUIRED));

        assertionFactory.put(ID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getId(),
                ID_REQUIRED));
        assertionFactory.put(CURRENCY_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getCurrency(),
                CURRENCY_REQUIRED));
        assertionFactory.put(LAST_UPDATED_REQUIRED, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getLastUpdated(), LAST_UPDATED_REQUIRED));
        assertionFactory.put(END_DATE_TIME, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getEndDateTime(), END_DATE_TIME)
        );
        assertionFactory.put(START_DATE_TIME_REQUIRED, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getStartDateTime(), START_DATE_TIME_REQUIRED));
        assertionFactory.put(KWH_REQUIRED, (expectedParams, actual) -> compareFloatIncludeWildCard(
                expectedParams, actual.getKwh(), KWH_REQUIRED));
        assertionFactory.put(CDR_TOKEN_REQUIRED, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getCdrToken(), CDR_TOKEN_REQUIRED, CdrToken.class));
        assertionFactory.put(AUTH_METHOD_REQUIRED, (expectedParams, actual) -> compareEnumsIncludeWildCard(
                expectedParams, actual.getAuthMethod(), AUTH_METHOD_REQUIRED));
        assertionFactory.put(LOCATION_ID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getLocationId(), LOCATION_ID_REQUIRED));
        assertionFactory.put(EVSE_UID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getEvseUid(), EVSE_UID_REQUIRED));
        assertionFactory.put(CONNECTOR_ID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getCountryCode(), CONNECTOR_ID_REQUIRED));
        assertionFactory.put(STATUS_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getStatus().value(), STATUS_REQUIRED));
        assertionFactory.put(AUTHORIZATION_REFERENCE, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getAuthorizationReference(), AUTHORIZATION_REFERENCE));
        assertionFactory.put(METER_ID, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getMeterId(), METER_ID));
        assertionFactory.put(CHARGING_PERIODS, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getChargingPeriods(), CHARGING_PERIODS, ChargingPeriod.class));
    }

    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, SessionDto message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<SessionDto> getClazz() {
        return SessionDto.class;
    }
}
