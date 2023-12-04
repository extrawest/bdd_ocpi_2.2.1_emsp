package com.extrawest.bdd_cpo_ocpi.validation.request;

import com.extrawest.bdd_cpo_ocpi.utils.Generators;
import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionRequestBddHandler
        extends OutgoingMessageFieldsFactory<SessionDto>
        implements RequestMessageFactory<SessionDto> {

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
    public static final String TOTAL_COST = "total_cost";

    @PostConstruct
    private void init() {

        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(COUNTRY_CODE_REQUIRED, (req, countryCode) -> req.setCountryCode(
                getStringOrRandom(countryCode, 2)));
        this.requiredFieldsSetup.put(PARTY_ID_REQUIRED, (req, partyId) -> req.setPartyId(
                getStringOrRandom(partyId, 3)));
        this.requiredFieldsSetup.put(ID_REQUIRED, (req, id) -> req.setId(getStringOrRandom(id, 36)));
        this.requiredFieldsSetup.put(CURRENCY_REQUIRED, (req, currency) -> req.setCurrency(
                getStringOrRandom(currency, 3)));
        this.requiredFieldsSetup.put(LAST_UPDATED_REQUIRED, (req, lastUpdated) -> req.setLastUpdated(
                parseLocalDateTimeOrGenerateRandom(lastUpdated, LAST_UPDATED_REQUIRED)));
        this.requiredFieldsSetup.put(START_DATE_TIME_REQUIRED, (req, startDateTime) -> req.setStartDateTime(
                parseLocalDateTimeOrGenerateRandom(startDateTime, START_DATE_TIME_REQUIRED)));
        this.requiredFieldsSetup.put(KWH_REQUIRED, (req, kwh) -> req.setKwh(
                getFloatOrRandom(kwh, KWH_REQUIRED)));
        this.requiredFieldsSetup.put(CDR_TOKEN_REQUIRED, (req, cdrToken) -> req.setCdrToken(
                parseObjectOrRandom(cdrToken, CDR_TOKEN_REQUIRED, CdrToken.class, Generators::generateCdrToken)));
        this.requiredFieldsSetup.put(AUTH_METHOD_REQUIRED, (req, authMethod) -> req.setAuthMethod(
                parseEnumOrRandom(authMethod, AUTH_METHOD_REQUIRED, AuthMethod.class)));
        this.requiredFieldsSetup.put(LOCATION_ID_REQUIRED, (req, locationId) -> req.setLocationId(
                getStringOrRandom(locationId, 36)));
        this.requiredFieldsSetup.put(EVSE_UID_REQUIRED, (req, evseUid) -> req.setEvseUid(
                getStringOrRandom(evseUid, 36)));
        this.requiredFieldsSetup.put(CONNECTOR_ID_REQUIRED, (req, connectorId) -> req.setConnectorId(
                getStringOrRandom(connectorId, 36)));
        this.requiredFieldsSetup.put(STATUS_REQUIRED, (req, status) -> req.setStatus(
                parseEnumOrRandom(status, STATUS_REQUIRED, SessionStatus.class)));

        this.optionalFieldsSetup = Map.of(
                END_DATE_TIME, (req, endDateTime) -> {
                    if (nonEqual(wildCard, endDateTime)) {
                        req.setEndDateTime(LocalDateTime.parse(endDateTime));
                    }
                },
                AUTHORIZATION_REFERENCE, (req, authReference) -> {
                    if (nonEqual(wildCard, authReference)) {
                        req.setAuthorizationReference(authReference);
                    }
                },
                METER_ID, (req, meterId) -> {
                    if (nonEqual(wildCard, meterId)) {
                        req.setMeterId(meterId);
                    }
                },
                TOTAL_COST, (req, totalCost) -> {
                    if (nonEqual(wildCard, totalCost)) {
                        req.setTotalCost(parseModelFromJson(totalCost, TOTAL_COST, Price.class));
                    }
                },
                CHARGING_PERIODS, (req, chargingPeriods) -> {
                    if (nonEqual(wildCard, chargingPeriods)) {
                        ChargingPeriod[] chargingPeriodsArray =
                                parseModelsFromJson(chargingPeriods, CHARGING_PERIODS, ChargingPeriod.class);
                        req.setChargingPeriods(Arrays.asList(chargingPeriodsArray));
                    }
                }
        );
    }

    @Override
    public SessionDto createMessageWithValidatedParams(Map<String, String> params) {
        SessionDto sessionDTO = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + sessionDTO);
        return sessionDTO;
    }

}
