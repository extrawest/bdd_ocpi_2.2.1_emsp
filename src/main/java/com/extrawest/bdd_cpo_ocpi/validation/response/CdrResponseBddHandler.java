package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.CdrDTO;
import com.extrawest.ocpi.model.dto.TariffDTO;
import com.extrawest.ocpi.model.enums.AuthMethod;
import com.extrawest.ocpi.model.vo.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CdrResponseBddHandler extends IncomingMessageFieldsFactory<CdrDTO>
        implements ResponseMessageFactory<CdrDTO> {
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
        this.requiredFieldsSetup.put(END_DATE_TIME_REQUIRED, (req, endDateTime) -> {
            if (nonEqual(wildCard, endDateTime)) {
                req.setStartDateTime(LocalDateTime.parse(endDateTime));
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
        this.requiredFieldsSetup.put(CDR_LOCATION_REQUIRED, (req, cdrLocation) -> {
            if (nonEqual(wildCard, cdrLocation)) {
                CdrLocation cdrLocationObj = parseModelFromJson(cdrLocation, CDR_LOCATION_REQUIRED, CdrLocation.class);
                req.setCdrLocation(cdrLocationObj);
            }
        });
        this.requiredFieldsSetup.put(CHARGING_PERIODS_REQUIRED, (req, cp) -> {
            if (nonEqual(wildCard, cp)) {
                ChargingPeriod[] chargingPeriods = parseModelsFromJson(cp, CDR_LOCATION_REQUIRED, ChargingPeriod.class);
                req.setChargingPeriods(List.of(chargingPeriods));
            }
        });
        this.requiredFieldsSetup.put(TOTAL_COST_REQUIRED, (req, totalCost) -> {
            if (nonEqual(wildCard, totalCost)) {
                Price price = parseModelFromJson(totalCost, TOTAL_COST_REQUIRED, Price.class);
                req.setTotalCost(price);
            }
        });
        this.requiredFieldsSetup.put(TOTAL_ENERGY_REQUIRED, (req, totalEnergy) -> {
            if (nonEqual(wildCard, totalEnergy)) {
                req.setTotalEnergy(Float.parseFloat(totalEnergy));
            }
        });
        this.requiredFieldsSetup.put(TOTAL_TIME_REQUIRED, (req, totalTime) -> {
            if (nonEqual(wildCard, totalTime)) {
                req.setTotalTime(Float.parseFloat(totalTime));
            }
        });

        this.optionalFieldsSetup = new HashMap<>();
        this.optionalFieldsSetup.put(AUTHORIZATION_REFERENCE, (req, authRef) -> {
            if (nonEqual(wildCard, authRef)) {
                req.setAuthorizationReference(authRef);
            }
        });
        this.optionalFieldsSetup.put(METER_ID, (req, meterId) -> {
            if (nonEqual(wildCard, meterId)) {
                req.setMeterId(meterId);
            }
        });
        this.optionalFieldsSetup.put(TARIFFS, (req, tariffs) -> {
            if (nonEqual(wildCard, tariffs)) {
                TariffDTO[] tariffsDtos = parseModelsFromJson(tariffs, TARIFFS, TariffDTO.class);
                req.setTariffs(List.of(tariffsDtos));
            }
        });
        this.optionalFieldsSetup.put(SIGNED_DATA, (req, sd) -> {
            if (nonEqual(wildCard, sd)) {
                SignedData signedData = parseModelFromJson(sd, SIGNED_DATA, SignedData.class);
                req.setSignedData(signedData);
            }
        });
        this.optionalFieldsSetup.put(TOTAL_FIXED_COST, (req, totalFixedCost) -> {
            if (nonEqual(wildCard, totalFixedCost)) {
                req.setTotalFixedCost(parseModelFromJson(totalFixedCost, TOTAL_FIXED_COST, Price.class));
            }
        });
        this.optionalFieldsSetup.put(TOTAL_ENERGY_COST, (req, totalEnergyCost) -> {
            if (nonEqual(wildCard, totalEnergyCost)) {
                Price price = parseModelFromJson(totalEnergyCost, TOTAL_ENERGY_COST, Price.class);
                req.setTotalEnergyCost(price);
            }
        });
        this.optionalFieldsSetup.put(TOTAL_TIME_COST, (req, totalTimeCost) -> {
            if (nonEqual(wildCard, totalTimeCost)) {
                Price price = parseModelFromJson(totalTimeCost, TOTAL_TIME_COST, Price.class);
                req.setTotalTimeCost(price);
            }
        });
        this.optionalFieldsSetup.put(TOTAL_PARKING_TIME, (req, totalTimeCost) -> {
            if (nonEqual(wildCard, totalTimeCost)) {
                req.setTotalParkingTime(Float.parseFloat(totalTimeCost));
            }
        });
        this.optionalFieldsSetup.put(TOTAL_PARKING_COST, (req, totalParkingCost) -> {
            if (nonEqual(wildCard, totalParkingCost)) {
                req.setTotalParkingCost(parseModelFromJson(totalParkingCost, TOTAL_PARKING_COST, Price.class));
            }
        });
        this.optionalFieldsSetup.put(TOTAL_RESERVATION_COST, (req, totalReservationCost) -> {
            if (nonEqual(wildCard, totalReservationCost)) {
                req.setTotalReservationCost(
                        parseModelFromJson(totalReservationCost, TOTAL_RESERVATION_COST, Price.class));
            }
        });
        this.optionalFieldsSetup.put(REMARK, (req, remark) -> {
            if (nonEqual(wildCard, remark)) {
                req.setRemark(remark);
            }
        });
        this.optionalFieldsSetup.put(INVOICE_REFERENCE_ID, (req, invoiceId) -> {
            if (nonEqual(wildCard, invoiceId)) {
                req.setInvoiceReferenceId(invoiceId);
            }
        });
        this.optionalFieldsSetup.put(CREDIT, (req, credit) -> {
            if (nonEqual(wildCard, credit)) {
                req.setCredit(Boolean.parseBoolean(credit));
            }
        });
        this.optionalFieldsSetup.put(CREDIT_REFERENCE_ID, (req, creditId) -> {
            if (nonEqual(wildCard, creditId)) {
                req.setCreditReferenceId(creditId);
            }
        });
        this.optionalFieldsSetup.put(HOME_CHARGING_COMPENSATION, (req, homeChargingCompensation) -> {
            if (nonEqual(wildCard, homeChargingCompensation)) {
                req.setHomeChargingCompensation(Boolean.parseBoolean(homeChargingCompensation));
            }
        });

        this.assertionFactory = new HashMap<>();
        assertionFactory.put(COUNTRY_CODE_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getCountryCode(), COUNTRY_CODE_REQUIRED));
        assertionFactory.put(PARTY_ID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getPartyId(), PARTY_ID_REQUIRED));
        assertionFactory.put(ID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getId(), ID_REQUIRED));
        assertionFactory.put(CURRENCY_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getCurrency(), CURRENCY_REQUIRED));
        assertionFactory.put(LAST_UPDATED_REQUIRED, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getLastUpdated(), LAST_UPDATED_REQUIRED));
        assertionFactory.put(START_DATE_TIME_REQUIRED, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getStartDateTime(), START_DATE_TIME_REQUIRED));
        assertionFactory.put(CDR_TOKEN_REQUIRED, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getCdrToken(), CDR_TOKEN_REQUIRED, CdrToken.class));
        assertionFactory.put(AUTH_METHOD_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getAuthMethod().value(), AUTH_METHOD_REQUIRED));
        assertionFactory.put(AUTHORIZATION_REFERENCE, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getAuthorizationReference(), AUTHORIZATION_REFERENCE));
        assertionFactory.put(METER_ID, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getMeterId(), METER_ID));
        assertionFactory.put(END_DATE_TIME_REQUIRED, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getEndDateTime(), END_DATE_TIME_REQUIRED));
        assertionFactory.put(CDR_LOCATION_REQUIRED, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getCdrLocation(), CDR_LOCATION_REQUIRED, CdrLocation.class));
        assertionFactory.put(CHARGING_PERIODS_REQUIRED, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getChargingPeriods(), CHARGING_PERIODS_REQUIRED, ChargingPeriod.class));
        assertionFactory.put(TOTAL_COST_REQUIRED, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getTotalCost(), TOTAL_COST_REQUIRED, Price.class));
        assertionFactory.put(TOTAL_ENERGY_REQUIRED, (expectedParams, actual) -> compareFloatIncludeWildCard(
                expectedParams, actual.getTotalEnergy(), TOTAL_ENERGY_REQUIRED));
        assertionFactory.put(TOTAL_TIME_REQUIRED, (expectedParams, actual) -> compareFloatIncludeWildCard(
                expectedParams, actual.getTotalTime(), TOTAL_TIME_REQUIRED));
        assertionFactory.put(TARIFFS, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getTariffs(), TARIFFS, TariffDTO.class));
        assertionFactory.put(SIGNED_DATA, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getSignedData(), SIGNED_DATA, SignedData.class));
        assertionFactory.put(TOTAL_FIXED_COST, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getTotalFixedCost(), TOTAL_FIXED_COST, Price.class));
        assertionFactory.put(TOTAL_ENERGY_COST, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getTotalEnergyCost(), TOTAL_ENERGY_COST, Price.class));
        assertionFactory.put(TOTAL_PARKING_TIME, (expectedParams, actual) -> compareFloatIncludeWildCard(
                expectedParams, actual.getTotalParkingTime(), TOTAL_PARKING_TIME));
        assertionFactory.put(TOTAL_TIME_COST, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getTotalTimeCost(), TOTAL_TIME_COST, Price.class));
        assertionFactory.put(TOTAL_PARKING_COST, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getTotalParkingCost(), TOTAL_PARKING_COST, Price.class));
        assertionFactory.put(TOTAL_RESERVATION_COST, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getTotalReservationCost(), TOTAL_RESERVATION_COST, Price.class));
        assertionFactory.put(REMARK, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getRemark(), REMARK));
        assertionFactory.put(INVOICE_REFERENCE_ID, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getInvoiceReferenceId(), INVOICE_REFERENCE_ID));
        assertionFactory.put(CREDIT_REFERENCE_ID, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getCreditReferenceId(), CREDIT_REFERENCE_ID));
        assertionFactory.put(CREDIT, (expectedParams, actual) -> compareBooleanIncludeWildCard(
                expectedParams, actual.getCredit(), CREDIT));
        assertionFactory.put(HOME_CHARGING_COMPENSATION, (expectedParams, actual) -> compareBooleanIncludeWildCard(
                expectedParams, actual.getHomeChargingCompensation(), HOME_CHARGING_COMPENSATION));
    }

    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, CdrDTO message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<CdrDTO> getClazz() {
        return CdrDTO.class;
    }
}
