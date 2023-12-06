package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.DisplayText;
import com.extrawest.ocpi.model.dto.Price;
import com.extrawest.ocpi.model.dto.location.EnergyMix;
import com.extrawest.ocpi.model.dto.tariff.TariffDto;
import com.extrawest.ocpi.model.dto.tariff.TariffElement;
import com.extrawest.ocpi.model.enums.TariffType;
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
public class TariffResponseBddHandler extends IncomingMessageFieldsFactory<TariffDto>
        implements ResponseMessageFactory<TariffDto> {

    public static final String COUNTRY_CODE_REQUIRED = "country_code";
    public static final String PARTY_ID_REQUIRED = "party_id";
    public static final String ID_REQUIRED = "id";
    public static final String CURRENCY_REQUIRED = "currency";
    public static final String LAST_UPDATED_REQUIRED = "last_updated";
    public static final String ELEMENTS_REQUIRED = "elements";

    public static final String TYPE = "type";
    public static final String TARIFF_ALT_TEXT = "tariff_alt_text";
    public static final String TARIFF_ALT_URL = "tariff_alt_url";
    public static final String MIN_PRICE = "min_price";
    public static final String MAX_PRICE = "max_price";
    public static final String START_DATE_TIME = "start_date_time";
    public static final String END_DATE_TIME = "end_date_time";
    public static final String ENERGY_MIX = "energy_mix";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = Map.of(
                COUNTRY_CODE_REQUIRED, (req, model) -> {
                    if (nonEqual(wildCard, model)) {
                        req.setCountryCode(model);
                    }
                },
                PARTY_ID_REQUIRED, (req, partyId) -> {
                    if (nonEqual(wildCard, partyId)) {
                        req.setPartyId(partyId);
                    }
                },
                ID_REQUIRED, (req, id) -> {
                    if (nonEqual(wildCard, id)) {
                        req.setId(id);
                    }
                },
                CURRENCY_REQUIRED, (req, currency) -> {
                    if (nonEqual(wildCard, currency)) {
                        req.setCurrency(currency);
                    }
                },
                LAST_UPDATED_REQUIRED, (req, lastUpdated) -> {
                    if (nonEqual(wildCard, lastUpdated)) {
                        req.setLastUpdated(LocalDateTime.parse(lastUpdated));
                    }
                },
                ELEMENTS_REQUIRED, (req, elements) -> {
                    if (nonEqual(wildCard, elements)) {
                        TariffElement[] tariffElements =
                                parseModelsFromJson(elements, ELEMENTS_REQUIRED, TariffElement.class);
                        req.setElements(List.of(tariffElements));
                    }
                }
        );

        this.optionalFieldsSetup = Map.of(
                TYPE, (req, type) -> {
                    if (nonEqual(wildCard, type)) {
                        req.setType(TariffType.valueOf(type));
                    }
                },
                TARIFF_ALT_TEXT, (req, tariffAltText) -> {
                    if (nonEqual(wildCard, tariffAltText)) {
                        DisplayText[] displayTexts =
                                parseModelsFromJson(tariffAltText, TARIFF_ALT_TEXT, DisplayText.class);
                        req.setTariffAltText(List.of(displayTexts));
                    }
                },
                TARIFF_ALT_URL, (req, tariffAltUrl) -> {
                    if (nonEqual(wildCard, tariffAltUrl)) {
                        req.setTariffAltUrl(tariffAltUrl);
                    }
                },
                MIN_PRICE, (req, minPrice) -> {
                    if (nonEqual(wildCard, minPrice)) {
                        req.setMinPrice(parseModelFromJson(minPrice, MIN_PRICE, Price.class));
                    }
                },
                MAX_PRICE, (req, maxPrice) -> {
                    if (nonEqual(wildCard, maxPrice)) {
                        req.setMaxPrice(parseModelFromJson(maxPrice, MAX_PRICE, Price.class));
                    }
                },
                START_DATE_TIME, (req, startDateTime) -> {
                    if (nonEqual(wildCard, startDateTime)) {
                        req.setStartDateTime(LocalDateTime.parse(startDateTime));
                    }
                },
                END_DATE_TIME, (req, endDateTime) -> {
                    if (nonEqual(wildCard, endDateTime)) {
                        req.setEndDateTime(LocalDateTime.parse(endDateTime));
                    }
                },
                ENERGY_MIX, (req, energyMix) -> {
                    if (nonEqual(wildCard, energyMix)) {
                        req.setEnergyMix(parseModelFromJson(energyMix, ENERGY_MIX, EnergyMix.class));
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
        assertionFactory.put(ELEMENTS_REQUIRED, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getElements(), ELEMENTS_REQUIRED, TariffElement.class));
        assertionFactory.put(TYPE, (expectedParams, actual) -> compareEnumsIncludeWildCard(
                expectedParams, actual.getType(), TYPE));
        assertionFactory.put(MIN_PRICE, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getMinPrice(), MIN_PRICE, Price.class));
        assertionFactory.put(MAX_PRICE, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getMinPrice(), MAX_PRICE, Price.class));
        assertionFactory.put(TARIFF_ALT_URL, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getTariffAltUrl(), TARIFF_ALT_URL));
        assertionFactory.put(ENERGY_MIX, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getEnergyMix(), ENERGY_MIX, EnergyMix.class));
        assertionFactory.put(START_DATE_TIME, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getStartDateTime(), START_DATE_TIME));
        assertionFactory.put(END_DATE_TIME, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getEndDateTime(), END_DATE_TIME)
        );
    }

    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, TariffDto message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<TariffDto> getClazz() {
        return TariffDto.class;
    }
}
