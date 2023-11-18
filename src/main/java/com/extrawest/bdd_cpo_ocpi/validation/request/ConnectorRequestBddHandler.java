package com.extrawest.bdd_cpo_ocpi.validation.request;


import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
import com.extrawest.ocpi.model.dto.location.Connector;
import com.extrawest.ocpi.model.enums.ConnectorFormat;
import com.extrawest.ocpi.model.enums.ConnectorType;
import com.extrawest.ocpi.model.enums.PowerType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectorRequestBddHandler extends OutgoingMessageFieldsFactory<Connector>
        implements RequestMessageFactory<Connector> {
    public static final String ID_REQUIRED = "id";
    public static final String STANDART_REQUIRED = "standard";
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
        this.requiredFieldsSetup.put(ID_REQUIRED, (req, id) -> req.setConnectorId(getStringOrRandom(id, 36)));
        this.requiredFieldsSetup.put(LAST_UPDATED_REQUIRED, (req, lastUpdated) -> req.setLastUpdated(
                parseLocalDateTimeOrGenerateRandom(lastUpdated, LAST_UPDATED_REQUIRED)));
        this.requiredFieldsSetup.put(STANDART_REQUIRED, (req, standard) -> req.setStandard(
                parseEnumOrRandom(standard, STANDART_REQUIRED, ConnectorType.class)));
        this.requiredFieldsSetup.put(FORMAT_REQUIRED, (req, format) -> req.setFormat(
                parseEnumOrRandom(format, FORMAT_REQUIRED, ConnectorFormat.class)));
        this.requiredFieldsSetup.put(POWER_TYPE_REQUIRED, (req, powerType) -> req.setPowerType(
                parseEnumOrRandom(powerType, POWER_TYPE_REQUIRED, PowerType.class)));
        this.requiredFieldsSetup.put(MAX_VOLTAGE_REQUIRED, (req, maxVoltage) -> req.setMaxVoltage(
                getIntegerOrRandom(maxVoltage, MAX_VOLTAGE_REQUIRED)));
        this.requiredFieldsSetup.put(MAX_AMPERAGE_REQUIRED, (req, maxAmperage) -> req.setMaxAmperage(
                getIntegerOrRandom(maxAmperage, MAX_AMPERAGE_REQUIRED)));

        this.optionalFieldsSetup = new HashMap<>();
        this.optionalFieldsSetup.put(TARIFF_IDS, (req, tariffIds) -> {
            if (nonEqual(wildCard, tariffIds)) {
                List<String> ids = List.of(tariffIds.split(","));
                req.setTariffIds(ids);
            }
        });
        this.optionalFieldsSetup.put(MAX_ELECTRIC_POWER, (req, maxElectricPower) -> {
            if (nonEqual(wildCard, maxElectricPower)) {
                req.setMaxElectricPower(Integer.valueOf(maxElectricPower));
            }
        });
        this.optionalFieldsSetup.put(TERMS_AND_CONDITIONS, (req, termsAndConditions) -> {
            if (nonEqual(wildCard, termsAndConditions)) {
                req.setTermsAndConditions(termsAndConditions);
            }
        });
    }

    @Override
    public Connector createMessageWithValidatedParams(Map<String, String> params) {
        Connector connector = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + connector);
        return connector;
    }
}
