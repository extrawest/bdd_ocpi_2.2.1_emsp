package com.extrawest.bdd_cpo_ocpi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorMessage {
    INVALID_REQUIRED_PARAM("Invalid required '%s' parameter for '%s' message. "),
    INVALID_FIELD_VALUE("'%s' has invalid '%s' field value. %s. "),
    REDUNDANT_EXPECTED_PARAM("Redundant '%s' expected parameter for '%s' message. " +
            "Required fields: %s. Optional fields: %s"),
    NON_MATCH_FIELDS("%s has non match field(s): %s"),
    NON_MATCH_ROW("Not found in response: %s"),
    NON_MATCH_RESPONSE_LIST_SIZE("Response list and expected list aren't equal in size"),
    ANY_NOT_ALLOWED("\"Any\" is not allowed in expected list"),
    EXPECTED_AND_ACTUAL_LISTS_NOT_EQUALS("Expected and actual lists differs"),
    BUG_CREATING_INSTANCE("Unexpected bug: can't create instance of %s. "),
    BUG_PARSING_MODEL("Unexpected bug: can't parse model %s to string. "),
    WRONG_INSTANCE_OF("Requested method available only for %s inherit. "),
    UNEXPECTED_MESSAGE_RECEIVED("Unexpected %s message received: %s. "),
    INVALID_INCOMING_FACTORY("%s message doesn't have incoming factory. "),
    INVALID_OUTGOING_FACTORY("%s message doesn't have outgoing factory. "),
    INVALID_RESPONSE_TYPE("Type is not implemented. Response: "),
    INVALID_CONFIRMATION_TYPE("Type is not implemented. Confirmation: "),
    UNKNOWN_HTTP_METHOD("Not implemented http method %s"),
    FAILED_TO_REQUEST("Failed to execute pre-configured request"),
    FILE_NOT_FOUND("File %s was not found"),
    VERSION_IS_NOT_2_2_1("Version is %s. Required version is 2.2.1"),
    MODULE_NOT_IMPLEMENTED("Module %s is not implemented on eMSP side. Implemented modules are: %s"),
    VERSION_2_2_1_NOT_SUPPORTED("eMSP doesn't support version 2.2.1"),
    CPO_NOT_REGISTERED("CPO was not registered and haven't received emps's endpoints"),

    NOT_MATCHING_MODEL_CONSTRAINT("Response with type %s violates ocpi 2.2.1 model constraint(s): %s"),
    MODEL_CANT_BE_INSTANTIATED("Model %s can not be instantiated: %s"),
    EMPTY_RESPONSE_RECEIVED("Empty response received"),
    EMPTY_RESPONSE_DATA_RECEIVED("Response has no data value"),
    EMPTY_EXPECTED_VALUE("Expected value was not provided");

    private final String value;

}
