package com.extrawest.bdd_cpo_ocpi.validation;

import com.extrawest.bdd_cpo_ocpi.exception.AssertionException;
import com.extrawest.bdd_cpo_ocpi.exception.BddTestingException;
import com.extrawest.bdd_cpo_ocpi.exception.ValidationException;
import com.extrawest.ocpi.exception.PropertyConstraintException;
import com.extrawest.ocpi.model.OcpiResponseData;
import com.extrawest.ocpi.model.enums.VersionNumber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.extrawest.bdd_cpo_ocpi.exception.ApiErrorMessage.*;
import static java.util.Objects.isNull;

/**
 * requiredFieldsSetup(FIELD_NAME, BiConsumer) - must contain BiConsumer to set up field value from given string value.
 * Mandatory for all required fields of parametrized model.
 * optionalFieldsSetup(FIELD_NAME, BiConsumer) - must contain BiConsumer to set up field value from given string value.
 * Mandatory for all optional fields of parametrized model.
 * assertionFactory(FIELD_NAME, BiFunction) - must contain BiFunction to compare field value from given parameters
 * with relevant field of parametrized model. Mandatory for all parametrized model's fields.
 */

@Slf4j
@Component
public abstract class IncomingMessageFieldsFactory<T extends OcpiResponseData> {
    @Autowired
    @Setter
    protected ObjectMapper mapper;

    @Value("${wildcard:any}")
    protected String wildCard;
    protected String nonMatchMessage = "'%s', field %s has unexpected value.\nExpected: '%s' \nActual  : '%s'";

    protected Map<String, BiConsumer<T, String>> requiredFieldsSetup;
    protected Map<String, BiConsumer<T, String>> optionalFieldsSetup;
    protected Map<String, BiFunction<Map<String, String>, T, Boolean>> assertionFactory;


    protected boolean assertParamsAndMessageFields(Map<String, String> params, T actualMessage) {
        List<String> nonMatchFields = nonMatchValues(params, actualMessage);

        if (!nonMatchFields.isEmpty()) {
            log.warn("Non match fields: " + nonMatchFields);
            throw new AssertionException(
                    String.format(NON_MATCH_FIELDS.getValue(), getParameterizeClassName(), nonMatchFields));
        }
        return true;
    }

    private List<String> nonMatchValues(Map<String, String> expectedParams, T actual) {
        return assertionFactory.entrySet().stream()
                .filter(pair -> !pair.getValue().apply(expectedParams, actual))
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * @param params - map with parameters for validation.
     *               Validating parameters - Using SETTER methods(witch include validation) of parametrized model
     */
    protected T validateParamsViaLibModel(Map<String, String> params) {
        String messageType = getParameterizeClassName();
        validateForRequiredFields(params, messageType);

        String currentFieldName = "";
        T message;
        try {
            message = getParameterizeClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new BddTestingException(String.format(BUG_CREATING_INSTANCE.getValue(), messageType));
        }
        try {
            for (Map.Entry<String, String> pair : params.entrySet()) {
                currentFieldName = pair.getKey();
                if (requiredFieldsSetup.containsKey(currentFieldName)) {
                    requiredFieldsSetup.get(currentFieldName).accept(message, pair.getValue());
                } else if (optionalFieldsSetup.containsKey(currentFieldName)) {
                    optionalFieldsSetup.get(currentFieldName).accept(message, pair.getValue());
                } else {
                    throw new ValidationException(
                            String.format(REDUNDANT_EXPECTED_PARAM.getValue(), currentFieldName, messageType,
                                    requiredFieldsSetup.keySet(), optionalFieldsSetup.keySet()));
                }
            }
        } catch (PropertyConstraintException cause) {
            throw new ValidationException(
                    String.format(INVALID_FIELD_VALUE.getValue(), messageType, currentFieldName, cause.getMessage()));

        }
        return message;
    }

    private void validateForRequiredFields(Map<String, String> params, String messageType) {
        requiredFieldsSetup.keySet().forEach(field -> {
            if (!params.containsKey(field) || isNull(params.get(field))) {
                throw new ValidationException(
                        String.format(INVALID_REQUIRED_PARAM.getValue(), field, messageType));
            }
        });
    }

    protected <M extends OcpiResponseData> M parseModelFromJson(String value, String fieldName, Class<M> clazz) {
        try {
            log.info("JSON string for parsing: " + value);
            M model = mapper.readValue(value, clazz);
            log.info("Model parsed from string: " + model);
            return model;
        } catch (JsonProcessingException e) {
            throw new ValidationException(
                    String.format(INVALID_FIELD_VALUE.getValue(), getParameterizeClassName(), fieldName, value));
        }
    }

    protected <M extends OcpiResponseData> M[] parseModelsFromJson(String value, String fieldName, Class<M> clazz) {
        try {
            log.info("JSON string for array parsing: " + value);
            M[] result = mapper.readerForArrayOf(clazz).readValue(value);
            log.info("Models parsed from string: " + Arrays.toString(result));
            return result;
        } catch (JsonProcessingException e) {
            throw new ValidationException(
                    String.format(INVALID_FIELD_VALUE.getValue(), getParameterizeClassName(), fieldName, value));
        }
    }

    protected Class<T> getParameterizeClass() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<T>) paramType.getActualTypeArguments()[0];
    }

    protected String getParameterizeClassName() {
        Class<T> tClass = getParameterizeClass();
        return tClass.getSimpleName();
    }

    protected boolean nonEqual(Object firstValue, Object secondValue) {
        return !Objects.equals(firstValue, secondValue);
    }

    protected boolean compareStringsIncludeWildCard(Map<String, String> expectedParams,
                                                    String actual, String fieldName) {
        String expected = expectedParams.get(fieldName);
        boolean result = Objects.equals(expected, wildCard) || Objects.equals(expected, actual);
        if (!result) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return result;
    }

    protected boolean compareBooleanIncludeWildCard(Map<String, String> expectedParams,
                                                    Boolean actual, String fieldName) {
        String expected = expectedParams.get(fieldName);
        boolean result = Objects.equals(expected, wildCard) ||
                (actual == null && expected == null) ||
                (actual != null && Objects.equals(expected, actual.toString()));
        if (!result) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return result;
    }


    protected boolean compareFloatIncludeWildCard(Map<String, String> expectedParams,
                                                  Float actual, String fieldName) {
        String expected = expectedParams.get(fieldName);
        boolean result = Objects.equals(expected, wildCard) || Objects.equals(Float.valueOf(expected), actual);
        if (!result) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return result;
    }

    protected boolean compareIntegerIncludeWildCard(Map<String, String> expectedParams,
                                                    Integer actual, String fieldName) {
        String expected = expectedParams.get(fieldName);
        boolean result = Objects.equals(expected, wildCard) ||
                (actual == null && expected == null) ||
                (actual != null && Objects.equals(expected, String.valueOf(actual)));

        if (!result) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return result;
    }


    protected boolean compareDateTimeIncludeWildCard(Map<String, String> expectedParams,
                                                     LocalDateTime actual, String fieldName) {
        String expected = expectedParams.get(fieldName);
        boolean result = Objects.equals(expected, wildCard)
                || Objects.equals(actual, LocalDateTime.parse(expected));

        if (!result) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return result;
    }


    protected <M extends OcpiResponseData> boolean compareObjectIncludeWildCard(Map<String, String> expectedParams,
                                                                                M actual, String fieldName, Class<M> clazz) {
        String expected = expectedParams.get(fieldName);
        if (expected == null && actual == null) return true;
        if (Objects.equals(expected, wildCard)) return true;
        M expectedModel = parseModelFromJson(expected, fieldName, clazz);
        boolean result = actual.equals(expectedModel);
        if (!result) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return result;
    }

    protected <M extends OcpiResponseData> boolean compareListIncludeWildCard(Map<String, String> expectedParams,
                                                                              List<M> actual, String fieldName,
                                                                              Class<M> clazz) {
        String expected = expectedParams.get(fieldName);
        if (expected == null && actual == null) return true;
        if (Objects.equals(expected, wildCard)) return true;

        M[] expectedArray = parseModelsFromJson(expectedParams.get(fieldName), fieldName, clazz);
        boolean isEqualCollection = CollectionUtils.isEqualCollection(List.of(expectedArray), actual);
        if (!isEqualCollection) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return isEqualCollection;
    }

    protected boolean compareStringListIncludeWildCard(Map<String, String> expectedParams,
                                                       List<String> actual, String fieldName) {
        String expected = expectedParams.get(fieldName);
        if (expected == null && actual == null) return true;
        if (Objects.equals(expected, wildCard)) return true;
        if (expected == null) return false;

        List<String> ids = Stream.of(expected.split(",")).map(String::trim).collect(Collectors.toList());
        boolean isEqualCollection = CollectionUtils.isEqualCollection(ids, actual);
        if (!isEqualCollection) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return isEqualCollection;
    }

    protected <E> boolean compareEnumsIncludeWildCard(Map<String, String> expectedParams,
                                                      E actual, String fieldName) {
        String expected = expectedParams.get(fieldName);
        boolean result = Objects.equals(expected, wildCard) || Objects.equals(expected, actual);
        if (!result) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return result;
    }

    protected boolean compareVersionNumberIncludeWildCard(Map<String, String> expectedParams,
                                                          VersionNumber actual, String fieldName) {
        String expected = expectedParams.get(fieldName);
        boolean result = Objects.equals(expected, wildCard) || Objects.equals(expected, actual.value());
        if (!result) {
            log.warn(String.format(nonMatchMessage, getParameterizeClassName(), fieldName, expected, actual));
        }
        return result;
    }

}
