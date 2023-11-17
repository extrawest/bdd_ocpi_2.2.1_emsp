package com.extrawest.bdd_cpo_ocpi.validation.request;

import com.extrawest.bdd_cpo_ocpi.utils.Generators;
import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
import com.extrawest.ocpi.model.dto.TariffDTO;
import com.extrawest.ocpi.model.dto.TariffElementDTO;
import com.extrawest.ocpi.model.enums.TariffType;
import com.extrawest.ocpi.model.vo.DisplayText;
import com.extrawest.ocpi.model.vo.EnergyMix;
import com.extrawest.ocpi.model.vo.Price;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TariffRequestBddHandler extends OutgoingMessageFieldsFactory<TariffDTO>
        implements RequestMessageFactory<TariffDTO> {

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
                COUNTRY_CODE_REQUIRED, (req, countryCode) -> req.setCountryCode(
                        getStringOrRandom(countryCode, 2)),
                PARTY_ID_REQUIRED, (req, partyId) -> req.setPartyId(
                        getStringOrRandom(partyId, 3)),
                ID_REQUIRED, (req, id) -> req.setId(getStringOrRandom(id, 5)),
                CURRENCY_REQUIRED, (req, currency) -> req.setCurrency(
                        getStringOrRandom(currency, 3)),
                LAST_UPDATED_REQUIRED, (req, lastUpdated) -> req.setLastUpdated(
                        parseLocalDateTimeOrGenerateRandom(lastUpdated, LAST_UPDATED_REQUIRED)),
                ELEMENTS_REQUIRED, (req, elements) -> req.setElements(
                        parseToListOrGenerateRandom(elements, ELEMENTS_REQUIRED, TariffElementDTO.class,
                                Generators::generateTariffElement))
        );

        this.optionalFieldsSetup = Map.of(
                TYPE, (req, providedVal) -> {
                    if (nonEqual(wildCard, providedVal)) {
                        req.setType(parseEnum(providedVal, TYPE, TariffType.class));
                    }
                },
                TARIFF_ALT_TEXT, (req, text) -> {
                    if (nonEqual(wildCard, text)) {
                        DisplayText[] displayTexts = parseModelsFromJson(text, TARIFF_ALT_TEXT, DisplayText.class);
                        req.setTariffAltText(List.of(displayTexts));
                    }
                },
                TARIFF_ALT_URL, (req, providedVal) -> {
                    if (nonEqual(wildCard, providedVal)) {
                        req.setTariffAltUrl(providedVal);
                    }
                },
                MIN_PRICE, (req, minPrice) -> {
                    if (nonEqual(wildCard, minPrice)) {
                        Price price = parseModelFromJson(minPrice, MIN_PRICE, Price.class);
                        req.setMinPrice(price);
                    }
                },
                MAX_PRICE, (req, maxPrice) -> {
                    if (nonEqual(wildCard, maxPrice)) {
                        Price price = parseModelFromJson(maxPrice, MAX_PRICE, Price.class);
                        req.setMinPrice(price);
                    }
                },
                START_DATE_TIME, (req, dateTime) -> {
                    if (nonEqual(wildCard, dateTime)) {
                        LocalDateTime localDateTime = parseLocalDateTime(dateTime, MAX_PRICE);
                        req.setStartDateTime(localDateTime);
                    }
                },
                END_DATE_TIME, (req, dateTime) -> {
                    if (nonEqual(wildCard, dateTime)) {
                        LocalDateTime localDateTime = parseLocalDateTime(dateTime, END_DATE_TIME);
                        req.setStartDateTime(localDateTime);
                    }
                },
                ENERGY_MIX, (req, energyMix) -> {
                    if (nonEqual(wildCard, energyMix)) {
                        EnergyMix energy = parseModelFromJson(energyMix, ENERGY_MIX, EnergyMix.class);
                        req.setEnergyMix(energy);
                    }
                }
        );
    }

    @Override
    public TariffDTO createMessageWithValidatedParams(Map<String, String> params) {
        TariffDTO tariffDTO = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + tariffDTO);
        return tariffDTO;
    }
}
