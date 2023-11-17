package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.enums.ConnectorFormat;
import com.extrawest.ocpi.model.enums.ConnectorType;
import com.extrawest.ocpi.model.enums.PowerType;
import com.extrawest.ocpi.model.vo.Connector;
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
public class ConnectorResponseBddHandler extends IncomingMessageFieldsFactory<Connector>
        implements ResponseMessageFactory<Connector> {
    public static final String ID_REQUIRED = "id";
    public static final String STANDARD_REQUIRED = "standard";
    public static final String FORMAT_REQUIRED = "format";
    public static final String POWER_TYPE_REQUIRED = "power_type";
    public static final String MAX_VOLTAGE_REQUIRED = "max_voltage";
    public static final String MAX_AMPERAGE_REQUIRED = "max_amperage";
    public static final String LAST_UPDATED_REQUIRED = "last_updated";

    public static final String MAX_ELECTRIC_POWER = "max_electric_power";
    public static final String TARIFF_IDS = "tariff_ids";
    public static final String TERMS_AND_CONDITIONS = "terms_and_conditions";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(ID_REQUIRED, (req, id) -> {
            if (nonEqual(wildCard, id)) {
                req.setConnectorId(id);
            }
        });
        this.requiredFieldsSetup.put(LAST_UPDATED_REQUIRED, (req, lastUpdated) -> {
            if (nonEqual(wildCard, lastUpdated)) {
                req.setLastUpdated(LocalDateTime.parse(lastUpdated));
            }
        });
        this.requiredFieldsSetup.put(STANDARD_REQUIRED, (req, standard) -> {
            if (nonEqual(wildCard, standard)) {
                req.setStandard(ConnectorType.valueOf(standard));
            }
        });
        this.requiredFieldsSetup.put(FORMAT_REQUIRED, (req, format) -> {
            if (nonEqual(wildCard, format)) {
                req.setFormat(ConnectorFormat.valueOf(format));
            }
        });
        this.requiredFieldsSetup.put(POWER_TYPE_REQUIRED, (req, powerType) -> {
            if (nonEqual(wildCard, powerType)) {
                req.setPowerType(PowerType.valueOf(powerType));
            }
        });
        this.requiredFieldsSetup.put(MAX_VOLTAGE_REQUIRED, (req, maxVoltage) -> {
            if (nonEqual(wildCard, maxVoltage)) {
                req.setMaxVoltage(Integer.valueOf(maxVoltage));
            }
        });
        this.requiredFieldsSetup.put(MAX_AMPERAGE_REQUIRED, (req, maxAmperage) -> {
            if (nonEqual(wildCard, maxAmperage)) {
                req.setMaxAmperage(Integer.valueOf(maxAmperage));
            }
        });


        this.optionalFieldsSetup = new HashMap<>();
        this.optionalFieldsSetup.put(MAX_ELECTRIC_POWER, (req, maxElectricPower) -> {
            if (nonEqual(wildCard, maxElectricPower)) {
                req.setMaxElectricPower(Integer.valueOf(maxElectricPower));
            }
        });
        this.optionalFieldsSetup.put(TARIFF_IDS, (req, tariffIds) -> {
            if (nonEqual(wildCard, tariffIds)) {
                List<String> ids = List.of(tariffIds.split(","));
                req.setTariffIds(ids);
            }
        });
        this.optionalFieldsSetup.put(TERMS_AND_CONDITIONS, (req, termsAndConditions) -> {
            if (nonEqual(wildCard, termsAndConditions)) {
                req.setTermsAndConditions(termsAndConditions);
            }
        });

        this.assertionFactory = new HashMap<>();
        assertionFactory.put(ID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getConnectorId(), ID_REQUIRED));
        assertionFactory.put(STANDARD_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getStandard().value(), STANDARD_REQUIRED));
        assertionFactory.put(FORMAT_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getFormat().value(), FORMAT_REQUIRED));
        assertionFactory.put(POWER_TYPE_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getPowerType().value(), POWER_TYPE_REQUIRED));
        assertionFactory.put(LAST_UPDATED_REQUIRED, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getLastUpdated(), LAST_UPDATED_REQUIRED));
        assertionFactory.put(MAX_VOLTAGE_REQUIRED, (expectedParams, actual) -> compareIntegerIncludeWildCard(
                expectedParams, actual.getMaxVoltage(), MAX_VOLTAGE_REQUIRED));
        assertionFactory.put(MAX_AMPERAGE_REQUIRED, (expectedParams, actual) -> compareIntegerIncludeWildCard(
                expectedParams, actual.getMaxAmperage(), MAX_AMPERAGE_REQUIRED));
        assertionFactory.put(MAX_ELECTRIC_POWER, (expectedParams, actual) -> compareIntegerIncludeWildCard(
                expectedParams, actual.getMaxElectricPower(), MAX_ELECTRIC_POWER));
        assertionFactory.put(TARIFF_IDS, (expectedParams, actual) -> compareStringListIncludeWildCard(
                expectedParams, actual.getTariffIds(), TARIFF_IDS));
        assertionFactory.put(TERMS_AND_CONDITIONS, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getTermsAndConditions(), TERMS_AND_CONDITIONS));
    }

    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, Connector message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<Connector> getClazz() {
        return Connector.class;
    }
}
