package com.extrawest.bdd_cpo_ocpi.validation;

import com.extrawest.bdd_cpo_ocpi.models.enums.ImplementedMessageType;
import com.extrawest.ocpi.model.markers.OcpiRequestData;
import com.extrawest.ocpi.model.markers.OcpiResponseData;

import java.util.Map;

public interface AssertionAndValidationService {
    RequestMessageFactory<? extends OcpiRequestData> getOutgoingRequestFactory(ImplementedMessageType type);
    ResponseMessageFactory<? extends OcpiResponseData> getIncomingResponseFactory(ImplementedMessageType type);
    ImplementedMessageType validateAndAssertFields(OcpiResponseData ocpiData,
                                                   Map<String, String> parameters);
}
