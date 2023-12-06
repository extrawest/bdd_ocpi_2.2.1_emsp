package com.extrawest.bdd_cpo_ocpi.validation;

import com.extrawest.ocpi.model.markers.OcpiRequestData;

import java.util.Map;

public interface RequestMessageFactory<T extends OcpiRequestData> {
    T createMessageWithValidatedParams(Map<String, String> params);
}
