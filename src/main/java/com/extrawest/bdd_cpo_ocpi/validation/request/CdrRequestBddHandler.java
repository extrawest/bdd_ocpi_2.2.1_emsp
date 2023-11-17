package com.extrawest.bdd_cpo_ocpi.validation.request;

import com.extrawest.bdd_cpo_ocpi.utils.Generators;
import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
import com.extrawest.ocpi.model.dto.CdrDTO;
import com.extrawest.ocpi.model.enums.AuthMethod;
import com.extrawest.ocpi.model.vo.CdrLocation;
import com.extrawest.ocpi.model.vo.CdrToken;
import com.extrawest.ocpi.model.vo.ChargingPeriod;
import com.extrawest.ocpi.model.vo.Price;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CdrRequestBddHandler extends OutgoingMessageFieldsFactory<CdrDTO>
        implements RequestMessageFactory<CdrDTO> {
    public static final String COUNTRY_CODE_REQUIRED = "country_code";
    public static final String PARTY_ID_REQUIRED = "party_id";
    public static final String ID_REQUIRED = "id";
    public static final String START_DATE_TIME_REQUIRED = "start_date_time";
    public static final String END_DATE_TIME_REQUIRED = "end_date_time";
    public static final String CDR_TOKEN_REQUIRED = "cdr_token";
    public static final String AUTH_METHOD_REQUIRED = "auth_method";
    public static final String CDR_LOCATION_REQUIRED = "cdr_location";
    public static final String CURRENCY_REQUIRED = "currency";
    public static final String CHARGING_PERIODS_REQUIRED = "charging_periods";
    public static final String TOTAL_COST_REQUIRED = "total_cost";
    public static final String TOTAL_ENERGY_REQUIRED = "total_energy";
    public static final String TOTAL_TIME_REQUIRED = "total_time";
    public static final String LAST_UPDATED_REQUIRED = "last_updated";

    public static final String AUTHORIZATION_REFERENCE = "authorization_reference";
    public static final String METER_ID = "meter_id";
    public static final String TARIFFS = "tariffs";
    public static final String SIGNED_DATA = "signed_data";
    public static final String TOTAL_FIXED_COST = "total_fixed_cost";
    public static final String TOTAL_ENERGY_COST = "total_energy_cost";
    public static final String TOTAL_TIME_COST = "total_time_cost";
    public static final String TOTAL_PARKING_TIME = "total_parking_time";
    public static final String TOTAL_PARKING_COST = "total_parking_cost";
    public static final String TOTAL_RESERVATION_COST = "total_reservation_cost";
    public static final String REMARK = "remark";
    public static final String INVOICE_REFERENCE_ID = "invoice_reference_id";
    public static final String CREDIT = "credit";
    public static final String CREDIT_REFERENCE_ID = "credit_reference_id";
    public static final String HOME_CHARGING_COMPENSATION = "home_charging_compensation";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(COUNTRY_CODE_REQUIRED, (req, countryCode) -> req.setCountryCode(
                getStringOrRandom(countryCode, 2)));
        this.requiredFieldsSetup.put(PARTY_ID_REQUIRED, (req, partyId) -> req.setPartyId(
                getStringOrRandom(partyId, 3)));
        this.requiredFieldsSetup.put(ID_REQUIRED, (req, id) -> req.setId(getStringOrRandom(id, 5)));
        this.requiredFieldsSetup.put(CURRENCY_REQUIRED, (req, currency) -> req.setCurrency(
                getStringOrRandom(currency, 3)));
        this.requiredFieldsSetup.put(START_DATE_TIME_REQUIRED, (req, startDateTime) -> req.setStartDateTime(
                parseLocalDateTimeOrGenerateRandom(startDateTime, START_DATE_TIME_REQUIRED)));
        this.requiredFieldsSetup.put(END_DATE_TIME_REQUIRED, (req, endDateTime) -> req.setEndDateTime(
                parseLocalDateTimeOrGenerateRandom(endDateTime, END_DATE_TIME_REQUIRED)));
        this.requiredFieldsSetup.put(CDR_TOKEN_REQUIRED, (req, cdrToken) -> req.setCdrToken(
                parseObjectOrRandom(cdrToken, CDR_TOKEN_REQUIRED, CdrToken.class, Generators::generateCdrToken)));
        this.requiredFieldsSetup.put(AUTH_METHOD_REQUIRED, (req, authMethod) -> req.setAuthMethod(
                parseEnumOrRandom(authMethod, AUTH_METHOD_REQUIRED, AuthMethod.class)));
        this.requiredFieldsSetup.put(CDR_LOCATION_REQUIRED, (req, cdrLocation) -> req.setCdrLocation(
                parseObjectOrRandom(cdrLocation, CDR_LOCATION_REQUIRED,
                        CdrLocation.class, Generators::generateCdrLocation)));
        this.requiredFieldsSetup.put(CHARGING_PERIODS_REQUIRED, (req, chargingPeriods) -> req.setChargingPeriods(
                parseToListOrGenerateRandom(chargingPeriods, CHARGING_PERIODS_REQUIRED, ChargingPeriod.class,
                        Generators::generateChargingPeriod)));
        this.requiredFieldsSetup.put(TOTAL_COST_REQUIRED, (req, totalCost) -> req.setTotalCost(
                parseObjectOrRandom(totalCost, TOTAL_COST_REQUIRED, Price.class, Generators::generatePrice)));
        this.requiredFieldsSetup.put(TOTAL_ENERGY_REQUIRED, (req, totalEnergy) -> req.setTotalEnergy(
                getFloatOrRandom(totalEnergy, TOTAL_ENERGY_REQUIRED)));
        this.requiredFieldsSetup.put(TOTAL_TIME_REQUIRED, (req, totalTime) -> req.setTotalTime(
                getFloatOrRandom(totalTime, TOTAL_TIME_REQUIRED)));
        this.requiredFieldsSetup.put(LAST_UPDATED_REQUIRED, (req, lastUpdated) -> req.setLastUpdated(
                parseLocalDateTimeOrGenerateRandom(lastUpdated, LAST_UPDATED_REQUIRED)));

        this.optionalFieldsSetup = Collections.emptyMap();
    }

    @Override
    public CdrDTO createMessageWithValidatedParams(Map<String, String> params) {
        CdrDTO cdrDTO = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + cdrDTO);
        return cdrDTO;
    }
}
