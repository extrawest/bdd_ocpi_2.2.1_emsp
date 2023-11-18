package com.extrawest.bdd_cpo_ocpi.validation.request;

import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
import com.extrawest.ocpi.model.dto.LocationReferences;
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
public class LocationReferencesBddHandler
        extends OutgoingMessageFieldsFactory<LocationReferences>
        implements RequestMessageFactory<LocationReferences> {

    public static final String LOCATION_ID_REQUIRED = "location_id";
    public static final String EVSE_UIDS = "evse_uids";

    @PostConstruct
    private void init() {

        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(LOCATION_ID_REQUIRED, (req, locationId) -> req.setLocationId(
                getStringOrRandom(locationId, 36)));

        this.optionalFieldsSetup = Map.of(
                EVSE_UIDS, (req, evseUids) -> {
                    if (nonEqual(wildCard, evseUids)) {
                        List<String> uids = List.of(evseUids.split(","));
                        req.setEvseUids(uids);
                    }
                }
        );

    }

    @Override
    public LocationReferences createMessageWithValidatedParams(Map<String, String> params) {
        LocationReferences locationReferences = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + locationReferences);
        return locationReferences;
    }

}
