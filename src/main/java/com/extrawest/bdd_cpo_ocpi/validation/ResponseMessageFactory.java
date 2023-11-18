package com.extrawest.bdd_cpo_ocpi.validation;

import com.extrawest.ocpi.model.markers.OcpiResponseData;

import java.util.Map;

public interface ResponseMessageFactory<T extends OcpiResponseData> {
    void validateAndAssertFieldsWithParams(Map<String, String> params, T message);
    <M extends OcpiResponseData> Class<M> getClazz();
}
