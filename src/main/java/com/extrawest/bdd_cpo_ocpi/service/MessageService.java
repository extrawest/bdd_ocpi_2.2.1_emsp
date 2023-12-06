package com.extrawest.bdd_cpo_ocpi.service;

import com.extrawest.bdd_cpo_ocpi.models.enums.ImplementedMessageType;
import com.extrawest.ocpi.model.markers.OcpiRequestData;
import com.extrawest.ocpi.model.markers.OcpiResponseData;
import io.restassured.response.Response;

import java.util.Map;

public interface MessageService {
    OcpiRequestData createRequestBody(ImplementedMessageType type, Map<String, String> params);
    OcpiResponseData createResponseBody(ImplementedMessageType type, Response response);
    void validateResponseBody(Map<String, String> parameters, OcpiResponseData responseBody);
}
