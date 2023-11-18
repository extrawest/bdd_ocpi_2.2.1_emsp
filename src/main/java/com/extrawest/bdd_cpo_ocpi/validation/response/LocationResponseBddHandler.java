package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.location.GeoLocation;
import com.extrawest.ocpi.model.dto.location.Location;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LocationResponseBddHandler extends IncomingMessageFieldsFactory<Location>
        implements ResponseMessageFactory<Location> {
    public static final String COUNTRY_CODE_REQUIRED = "country_code";
    public static final String PARTY_ID_REQUIRED = "party_id";
    public static final String ID_REQUIRED = "id";
    public static final String LAST_UPDATED_REQUIRED = "last_updated";
    public static final String PUBLISH_REQUIRED = "publish";
    public static final String ADDRESS_REQUIRED = "address";
    public static final String CITY_REQUIRED = "city";
    public static final String COUNTRY_REQUIRED = "country";
    public static final String COORDINATES_REQUIRED = "coordinates";
    public static final String TIMEZONE_REQUIRED = "time_zone";

    public static final String PUBLISH_ALLOWED_TO = "publish_allowed_to";
    public static final String NAME = "name";
    public static final String POSTAL_CODE = "postal_code";
    public static final String STATE = "state";
    public static final String RELATED_LOCATIONS = "related_locations";
    public static final String PARKING_TYPE = "parking_type";
    public static final String EVSES = "evses";
    public static final String DIRECTIONS = "directions";
    public static final String OPERATOR = "operator";
    public static final String SUBOPERATOR = "suboperator";
    public static final String OWNER = "owner";
    public static final String FACILITIES = "facilities";
    public static final String OPENING_TIME = "opening_times";
    public static final String CHARGING_WHEN_CLOSED = "charging_when_closed";
    public static final String IMAGES = "images";
    public static final String ENERGY_MIX = "energy_mix";

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
        this.requiredFieldsSetup.put(LAST_UPDATED_REQUIRED, (req, lastUpdated) -> {
            if (nonEqual(wildCard, lastUpdated)) {
                req.setLastUpdated(LocalDateTime.parse(lastUpdated));
            }
        });
        this.requiredFieldsSetup.put(PUBLISH_REQUIRED, (req, publish) -> {
            if (nonEqual(wildCard, publish)) {
                req.setPublish(Boolean.parseBoolean(publish));
            }
        });
        this.requiredFieldsSetup.put(ADDRESS_REQUIRED, (req, address) -> {
            if (nonEqual(wildCard, address)) {
                req.setAddress(address);
            }
        });
        this.requiredFieldsSetup.put(CITY_REQUIRED, (req, city) -> {
            if (nonEqual(wildCard, city)) {
                req.setCity(city);
            }
        });
        this.requiredFieldsSetup.put(COUNTRY_REQUIRED, (req, country) -> {
            if (nonEqual(wildCard, country)) {
                req.setCountry(country);
            }
        });
        this.requiredFieldsSetup.put(COORDINATES_REQUIRED, (req, coordinates) -> {
            if (nonEqual(wildCard, coordinates)) {
                GeoLocation parsedCoordinates = parseModelFromJson(coordinates, COORDINATES_REQUIRED, GeoLocation.class);
                req.setCoordinates(parsedCoordinates);
            }
        });
        this.requiredFieldsSetup.put(TIMEZONE_REQUIRED, (req, timezone) -> {
            if (nonEqual(wildCard, timezone)) {
                req.setTimeZone(timezone);
            }
        });

        this.optionalFieldsSetup = new HashMap<>();

        this.assertionFactory = new HashMap<>();
        assertionFactory.put(COUNTRY_CODE_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getCountryCode(), COUNTRY_CODE_REQUIRED));
        assertionFactory.put(PARTY_ID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getPartyId(), PARTY_ID_REQUIRED));
        assertionFactory.put(ID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getId(), ID_REQUIRED));
        assertionFactory.put(PUBLISH_REQUIRED, (expectedParams, actual) -> compareBooleanIncludeWildCard(
                expectedParams, actual.getPublish(), PUBLISH_REQUIRED));
        assertionFactory.put(LAST_UPDATED_REQUIRED, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getLastUpdated(), LAST_UPDATED_REQUIRED));
        assertionFactory.put(ADDRESS_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getAddress(), ADDRESS_REQUIRED));
        assertionFactory.put(CITY_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getCity(), CITY_REQUIRED));
        assertionFactory.put(COUNTRY_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getCountry(), COUNTRY_REQUIRED));
        assertionFactory.put(COORDINATES_REQUIRED, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getCoordinates(), COORDINATES_REQUIRED, GeoLocation.class));
        assertionFactory.put(TIMEZONE_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getTimeZone(), TIMEZONE_REQUIRED));
    }

    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, Location message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<Location> getClazz() {
        return Location.class;
    }
}
