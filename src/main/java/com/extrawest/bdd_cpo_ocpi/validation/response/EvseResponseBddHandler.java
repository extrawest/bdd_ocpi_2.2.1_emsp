package com.extrawest.bdd_cpo_ocpi.validation.response;

import com.extrawest.bdd_cpo_ocpi.validation.IncomingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.ResponseMessageFactory;
import com.extrawest.ocpi.model.dto.DisplayText;
import com.extrawest.ocpi.model.dto.location.*;
import com.extrawest.ocpi.model.enums.Capability;
import com.extrawest.ocpi.model.enums.ParkingRestriction;
import com.extrawest.ocpi.model.enums.Status;
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
public class EvseResponseBddHandler extends IncomingMessageFieldsFactory<EVSE>
        implements ResponseMessageFactory<EVSE> {
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
        this.requiredFieldsSetup.put(UID_REQUIRED, (req, uid) -> {
            if (nonEqual(wildCard, uid)) {
                req.setUid(uid);
            }
        });
        this.requiredFieldsSetup.put(STATUS_REQUIRED, (req, status) -> {
            if (nonEqual(wildCard, status)) {
                req.setStatus(Status.valueOf(status));
            }
        });
        this.requiredFieldsSetup.put(CONNECTORS_REQUIRED, (req, connectors) -> {
            if (nonEqual(wildCard, connectors)) {
                Connector[] parsedConnectors = parseModelsFromJson(connectors, CONNECTORS_REQUIRED, Connector.class);
                req.setConnectors(List.of(parsedConnectors));
            }
        });
        this.requiredFieldsSetup.put(LAST_UPDATED_REQUIRED, (req, lastUpdated) -> {
            if (nonEqual(wildCard, lastUpdated)) {
                req.setLastUpdated(LocalDateTime.parse(lastUpdated));
            }
        });


        this.optionalFieldsSetup = new HashMap<>();
        this.optionalFieldsSetup.put(EVSE_ID, (req, evseId) -> {
            if (nonEqual(wildCard, evseId)) {
                req.setEvseId(evseId);
            }
        });

        this.optionalFieldsSetup.put(STATUS_SCHEDULE, (req, statusSchedule) -> {
            if (nonEqual(wildCard, statusSchedule)) {
                StatusSchedule[] scedules = parseModelsFromJson(statusSchedule, STATUS_SCHEDULE, StatusSchedule.class);
                req.setStatusSchedule(List.of(scedules));
            }
        });
        this.optionalFieldsSetup.put(CAPABILITIES, (req, capabilities) -> {
            if (nonEqual(wildCard, capabilities)) {
                Capability[] parsed = parseModelsFromJson(capabilities, CAPABILITIES, Capability.class);
                req.setCapabilities(List.of(parsed));
            }
        });
        this.optionalFieldsSetup.put(FLOOR_LEVEL, (req, floorLevel) -> {
            if (nonEqual(wildCard, floorLevel)) {
                req.setFloorLevel(floorLevel);
            }
        });
        this.optionalFieldsSetup.put(COORDINATES, (req, coordinates) -> {
            if (nonEqual(wildCard, coordinates)) {
                req.setCoordinates(parseModelFromJson(coordinates, COORDINATES, GeoLocation.class));
            }
        });
        this.optionalFieldsSetup.put(PHYSICAL_REFERENCE, (req, physicalReference) -> {
            if (nonEqual(wildCard, physicalReference)) {
                req.setPhysicalReference(physicalReference);
            }
        });
        this.optionalFieldsSetup.put(DIRECTIONS, (req, directions) -> {
            if (nonEqual(wildCard, directions)) {
                DisplayText[] parsed = parseModelsFromJson(directions, DIRECTIONS, DisplayText.class);
                req.setDirections(List.of(parsed));
            }
        });
        this.optionalFieldsSetup.put(PARKING_RESTRICTIONS, (req, parkingRestrictions) -> {
            if (nonEqual(wildCard, parkingRestrictions)) {
                ParkingRestriction[] parsed =
                        parseModelsFromJson(parkingRestrictions, PARKING_RESTRICTIONS, ParkingRestriction.class);
                req.setParkingRestrictions(List.of(parsed));
            }
        });
        this.optionalFieldsSetup.put(IMAGES, (req, images) -> {
            if (nonEqual(wildCard, images)) {
                Image[] parsed = parseModelsFromJson(images, IMAGES, Image.class);
                req.setImages(List.of(parsed));
            }
        });

        this.assertionFactory = new HashMap<>();
        assertionFactory.put(LAST_UPDATED_REQUIRED, (expectedParams, actual) -> compareDateTimeIncludeWildCard(
                expectedParams, actual.getLastUpdated(), LAST_UPDATED_REQUIRED));
        assertionFactory.put(UID_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getUid(), UID_REQUIRED));
        assertionFactory.put(STATUS_REQUIRED, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getStatus().value(), STATUS_REQUIRED));
        assertionFactory.put(CONNECTORS_REQUIRED, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getConnectors(), CONNECTORS_REQUIRED, Connector.class));
        assertionFactory.put(EVSE_ID, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getEvseId(), EVSE_ID));
        assertionFactory.put(STATUS_SCHEDULE, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getStatusSchedule(), STATUS_SCHEDULE, StatusSchedule.class));
        assertionFactory.put(CAPABILITIES, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getCapabilities(), CAPABILITIES, Capability.class));
        assertionFactory.put(FLOOR_LEVEL, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getFloorLevel(), FLOOR_LEVEL));
        assertionFactory.put(COORDINATES, (expectedParams, actual) -> compareObjectIncludeWildCard(
                expectedParams, actual.getCoordinates(), COORDINATES, GeoLocation.class));
        assertionFactory.put(PHYSICAL_REFERENCE, (expectedParams, actual) -> compareStringsIncludeWildCard(
                expectedParams, actual.getPhysicalReference(), PHYSICAL_REFERENCE));
        assertionFactory.put(DIRECTIONS, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getDirections(), DIRECTIONS, DisplayText.class));
        assertionFactory.put(PARKING_RESTRICTIONS, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getParkingRestrictions(), PARKING_RESTRICTIONS, ParkingRestriction.class));
        assertionFactory.put(IMAGES, (expectedParams, actual) -> compareListIncludeWildCard(
                expectedParams, actual.getImages(), IMAGES, Image.class));
    }

    @Override
    public void validateAndAssertFieldsWithParams(Map<String, String> params, EVSE message) {
        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return;
        }
        super.validateParamsViaLibModel(params);
        super.assertParamsAndMessageFields(params, message);
    }

    @Override
    public Class<EVSE> getClazz() {
        return EVSE.class;
    }
}
