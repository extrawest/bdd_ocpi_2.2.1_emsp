package com.extrawest.bdd_cpo_ocpi.validation.request;

import com.extrawest.bdd_cpo_ocpi.utils.Generators;
import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
import com.extrawest.ocpi.model.dto.LocationDTO;
import com.extrawest.ocpi.model.enums.Facility;
import com.extrawest.ocpi.model.enums.ParkingType;
import com.extrawest.ocpi.model.vo.*;
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
public class LocationRequestBddHandler extends OutgoingMessageFieldsFactory<LocationDTO>
        implements RequestMessageFactory<LocationDTO> {
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
        this.requiredFieldsSetup.put(COUNTRY_CODE_REQUIRED, (req, countryCode) -> req.setCountryCode(
                getStringOrRandom(countryCode, 2)));
        this.requiredFieldsSetup.put(PARTY_ID_REQUIRED, (req, partyId) -> req.setPartyId(
                getStringOrRandom(partyId, 3)));
        this.requiredFieldsSetup.put(ID_REQUIRED, (req, id) -> req.setId(getStringOrRandom(id, 36)));
        this.requiredFieldsSetup.put(PUBLISH_REQUIRED, (req, publish) -> req.setPublish(
                parseBooleanOrRandom(publish)));
        this.requiredFieldsSetup.put(ADDRESS_REQUIRED, (req, address) -> req.setAddress(
                getStringOrRandom(address, 45)));
        this.requiredFieldsSetup.put(CITY_REQUIRED, (req, city) -> req.setCity(
                getStringOrRandom(city, 45)));
        this.requiredFieldsSetup.put(COUNTRY_REQUIRED, (req, country) -> req.setCountry(
                getStringOrRandom(country, 3)));
        this.requiredFieldsSetup.put(COORDINATES_REQUIRED, (req, coordinates) -> req.setCoordinates(
                parseObjectOrRandom(coordinates, COORDINATES_REQUIRED, GeoLocation.class,
                        Generators::generateGeoLocation)));
        this.requiredFieldsSetup.put(TIMEZONE_REQUIRED, (req, timezone) -> req.setTimeZone(
                getStringOrRandom(timezone, 255)));
        this.requiredFieldsSetup.put(LAST_UPDATED_REQUIRED, (req, lastUpdated) -> req.setLastUpdated(
                parseLocalDateTimeOrGenerateRandom(lastUpdated, LAST_UPDATED_REQUIRED)));

        this.optionalFieldsSetup = new HashMap<>();
        this.optionalFieldsSetup.put(PUBLISH_ALLOWED_TO, (req, type) -> {
            PublishTokenType[] publishTokenTypes =
                    parseModelsFromJson(type, PUBLISH_ALLOWED_TO, PublishTokenType.class);
            req.setPublishAllowedTo(List.of(publishTokenTypes));
        });
        this.optionalFieldsSetup.put(NAME, LocationDTO::setName);
        this.optionalFieldsSetup.put(STATE, LocationDTO::setState);
        this.optionalFieldsSetup.put(RELATED_LOCATIONS, (req, locations) -> {
            AdditionalGeoLocation[] geoLocations =
                    parseModelsFromJson(locations, RELATED_LOCATIONS, AdditionalGeoLocation.class);
            req.setRelatedLocations(List.of(geoLocations));
        });
        this.optionalFieldsSetup.put(PARKING_TYPE, (req, type) -> req.setParkingType(
                parseEnum(type, PARKING_TYPE, ParkingType.class)));
        this.optionalFieldsSetup.put(EVSES, (req, locations) -> {
            Evse[] evses = parseModelsFromJson(locations, EVSES, Evse.class);
            req.setEvses(List.of(evses));
        });
        this.optionalFieldsSetup.put(DIRECTIONS, (req, text) -> {
            DisplayText[] displayTexts = parseModelsFromJson(text, DIRECTIONS, DisplayText.class);
            req.setDirections(List.of(displayTexts));
        });
        this.optionalFieldsSetup.put(OPERATOR, (req, operator) -> {
            BusinessDetails details = parseModelFromJson(operator, OPERATOR, BusinessDetails.class);
            req.setOperator(details);
        });
        this.optionalFieldsSetup.put(SUBOPERATOR, (req, operator) -> {
            BusinessDetails details = parseModelFromJson(operator, SUBOPERATOR, BusinessDetails.class);
            req.setSubOperator(details);
        });
        this.optionalFieldsSetup.put(OWNER, (req, operator) -> {
            BusinessDetails details = parseModelFromJson(operator, OWNER, BusinessDetails.class);
            req.setOwner(details);
        });
        this.optionalFieldsSetup.put(FACILITIES, (req, val) -> {
            List<Facility> listOfEnums = parseListOfEnums(val, FACILITIES, Facility.class);
            req.setFacilities(listOfEnums);
        });
        this.optionalFieldsSetup.put(OPENING_TIME, (req, val) -> {
            Hours hours = parseModelFromJson(val, OPENING_TIME, Hours.class);
            req.setOpeningTimes(hours);
        });
        this.optionalFieldsSetup.put(CHARGING_WHEN_CLOSED, (req, val) -> {
            req.setChargingWhenClosed(Boolean.parseBoolean(val));
        });
        this.optionalFieldsSetup.put(IMAGES, (req, val) -> {
            List<Image> listOfEnums = List.of(parseModelsFromJson(val, IMAGES, Image.class));
            req.setImages(listOfEnums);
        });
        this.optionalFieldsSetup.put(ENERGY_MIX, (req, energyMix) -> {
            EnergyMix energy = parseModelFromJson(energyMix, ENERGY_MIX, EnergyMix.class);
            req.setEnergyMix(energy);
        });
    }

    @Override
    public LocationDTO createMessageWithValidatedParams(Map<String, String> params) {
        LocationDTO locationDTO = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + locationDTO);
        return locationDTO;
    }
}
