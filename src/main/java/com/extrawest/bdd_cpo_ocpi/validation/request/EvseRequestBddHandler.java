package com.extrawest.bdd_cpo_ocpi.validation.request;

import com.extrawest.bdd_cpo_ocpi.utils.Generators;
import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
import com.extrawest.ocpi.model.dto.DisplayText;
import com.extrawest.ocpi.model.dto.location.*;
import com.extrawest.ocpi.model.enums.Capability;
import com.extrawest.ocpi.model.enums.ParkingRestriction;
import com.extrawest.ocpi.model.enums.Status;
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
public class EvseRequestBddHandler extends OutgoingMessageFieldsFactory<EVSE>
        implements RequestMessageFactory<EVSE> {
    public static final String UID_REQUIRED = "uid";
    public static final String LAST_UPDATED_REQUIRED = "last_updated";
    public static final String STATUS_REQUIRED = "status";
    public static final String CONNECTORS_REQUIRED = "connectors";

    public static final String EVSE_ID = "evse_id";
    public static final String STATUS_SCHEDULE = "status_schedule";
    public static final String CAPABILITIES = "capabilities";
    public static final String FLOOR_LEVEL = "floor_level";
    public static final String COORDINATES = "coordinates";
    public static final String PHYSICAL_REFERENCE = "physical_reference";
    public static final String DIRECTIONS = "directions";
    public static final String PARKING_RESTRICTIONS = "parking_restrictions";
    public static final String IMAGES = "images";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(UID_REQUIRED, (req, uid) -> req.setUid(
                getStringOrRandom(uid, 36)));
        this.requiredFieldsSetup.put(LAST_UPDATED_REQUIRED, (req, lastUpdated) -> req.setLastUpdated(
                parseLocalDateTimeOrGenerateRandom(lastUpdated, LAST_UPDATED_REQUIRED)));
        this.requiredFieldsSetup.put(STATUS_REQUIRED, (req, status) ->
                req.setStatus(parseEnumOrRandom(status, STATUS_REQUIRED, Status.class)));
        this.requiredFieldsSetup.put(CONNECTORS_REQUIRED, (req, connectors) -> req.setConnectors(
                parseToListOrGenerateRandom(connectors, CONNECTORS_REQUIRED,
                        Connector.class, Generators::generateConnector))
        );

        this.optionalFieldsSetup = new HashMap<>();

        this.optionalFieldsSetup.put(
                EVSE_ID, (req, evseId) -> {
                    if (nonEqual(wildCard, evseId)) {
                        req.setEvseId(evseId);
                    }
                });

        this.optionalFieldsSetup.put(
                STATUS_SCHEDULE, (req, statusSchedule) -> {
                    if (nonEqual(wildCard, statusSchedule)) {
                        StatusSchedule[] schedules =
                                parseModelsFromJson(statusSchedule, STATUS_SCHEDULE, StatusSchedule.class);
                        req.setStatusSchedule(List.of(schedules));
                    }
                });

        this.optionalFieldsSetup.put(
                CAPABILITIES, (req, capabilities) -> {
                    if (nonEqual(wildCard, capabilities)) {
                        List<Capability> listOfEnums = parseListOfEnums(capabilities, CAPABILITIES, Capability.class);
                        req.setCapabilities(listOfEnums);
                    }
                });

        this.optionalFieldsSetup.put(
                FLOOR_LEVEL, (req, floorLevel) -> {
                    if (nonEqual(wildCard, floorLevel)) {
                        req.setFloorLevel(floorLevel);
                    }
                });

        this.optionalFieldsSetup.put(
                COORDINATES, (req, coordinates) -> {
                    if (nonEqual(wildCard, coordinates)) {
                        GeoLocation geoLocation = parseModelFromJson(coordinates, COORDINATES, GeoLocation.class);
                        req.setCoordinates(geoLocation);
                    }
                });

        this.optionalFieldsSetup.put(
                PHYSICAL_REFERENCE, (req, physicalReference) -> {
                    if (nonEqual(wildCard, physicalReference)) {
                        req.setPhysicalReference(physicalReference);
                    }
                });

        this.optionalFieldsSetup.put(
                DIRECTIONS, (req, chargingPeriods) -> {
                    if (nonEqual(wildCard, chargingPeriods)) {
                        DisplayText[] displayTexts =
                                parseModelsFromJson(chargingPeriods, DIRECTIONS, DisplayText.class);
                        req.setDirections(List.of(displayTexts));
                    }
                });

        this.optionalFieldsSetup.put(
                PARKING_RESTRICTIONS, (req, pr) -> {
                    if (nonEqual(wildCard, pr)) {
                        req.setParkingRestrictions(parseListOfEnums(pr, PARKING_RESTRICTIONS, ParkingRestriction.class));
                    }
                });

        this.optionalFieldsSetup.put(
                IMAGES, (req, images) -> {
                    if (nonEqual(wildCard, images)) {
                        Image[] imgs = parseModelsFromJson(images, IMAGES, Image.class);
                        req.setImages(List.of(imgs));
                    }
                });
    }

    @Override
    public EVSE createMessageWithValidatedParams(Map<String, String> params) {
        EVSE evse = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + evse);
        return evse;
    }
}
