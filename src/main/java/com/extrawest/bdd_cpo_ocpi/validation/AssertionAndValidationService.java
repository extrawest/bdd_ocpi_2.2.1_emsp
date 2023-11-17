package com.extrawest.bdd_cpo_ocpi.validation;

import com.extrawest.bdd_cpo_ocpi.models.enums.ImplementedMessageType;
import com.extrawest.ocpi.model.OcpiRequestData;
import com.extrawest.ocpi.model.OcpiResponseData;

import java.util.Map;

public interface AssertionAndValidationService {
    RequestMessageFactory<? extends OcpiRequestData> getOutgoingRequestFactory(ImplementedMessageType type);
    ResponseMessageFactory<? extends OcpiResponseData> getIncomingResponseFactory(ImplementedMessageType type);
    ImplementedMessageType validateAndAssertFields(OcpiResponseData ocpiData,
                                                   Map<String, String> parameters);
}
