package com.extrawest.bdd_cpo_ocpi.validation;

import com.extrawest.bdd_cpo_ocpi.exception.BddTestingException;
import com.extrawest.bdd_cpo_ocpi.exception.ValidationException;
import com.extrawest.bdd_cpo_ocpi.utils.Generators;
import com.extrawest.ocpi.exception.PropertyConstraintException;
import com.extrawest.ocpi.model.OcpiRequestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.extrawest.bdd_cpo_ocpi.exception.ApiErrorMessage.*;
import static com.extrawest.bdd_cpo_ocpi.utils.Generators.*;
import static java.util.Objects.isNull;

/**
 * requiredFieldsSetup(FIELD_NAME, BiConsumer) - must contain BiConsumer to set up field value from given string value.
 * Mandatory for all required fields of parametrized model.
 * optionalFieldsSetup(FIELD_NAME, BiConsumer) - must contain BiConsumer to set up field value from given string value.
 * Mandatory for all optional fields of parametrized model.
 */

@Slf4j
@Component
public abstract class OutgoingMessageFieldsFactory<T extends OcpiRequestData> {
    @Autowired
    @Setter
    protected ObjectMapper mapper;

    @Value("${wildcard:any}")
    protected String wildCard;

    protected Map<String, BiConsumer<T, String>> requiredFieldsSetup;
    protected Map<String, BiConsumer<T, String>> optionalFieldsSetup;

    /**
     * @param params - map with parameters for validation.
     *               Validating parameters - Using SETTER methods(witch include validation) of parametrized model
     */
    protected T createMessageWithValidatedParamsViaLibModel(Map<String, String> params) {
        String messageType = getParameterizeClassName();
        String currentFieldName = "";
        T message;
        try {
            message = getParameterizeClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new BddTestingException(String.format(BUG_CREATING_INSTANCE.getValue(), messageType));
        }

        if (Objects.equals(params.size(), 1) && params.containsKey(wildCard)) {
            return createMessageWithDefaultRequiredParams(message);
        }
        validateMessageForRequiredFields(params);

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

    private T createMessageWithDefaultRequiredParams(T message) {
        for (Map.Entry<String, BiConsumer<T, String>> field : requiredFieldsSetup.entrySet()) {
            field.getValue().accept(message, wildCard);
        }
        return message;
    }

    private void validateMessageForRequiredFields(Map<String, String> params) {
        String messageType = getParameterizeClassName();
        requiredFieldsSetup.keySet().forEach(field -> {
            if (!params.containsKey(field) || isNull(params.get(field))) {
                throw new ValidationException(
                        String.format(INVALID_REQUIRED_PARAM.getValue(), field, messageType));
            }
        });
    }

    protected LocalDateTime parseLocalDateTimeOrGenerateRandom(String value,
                                                               String fieldName) {
        if (Objects.equals(value, wildCard) || isNull(value) || value.isEmpty()) {
            return Generators.randomDateTime();
        } else {
            return parseLocalDateTime(value, fieldName);
        }
    }

    protected LocalDateTime parseLocalDateTime(String value, String fieldName) {
        try {
            return LocalDateTime.parse(value);
        } catch (Exception cause) {
            throw new ValidationException(String.format(INVALID_REQUIRED_PARAM.getValue(),
                    fieldName, getParameterizeClassName()));
        }
    }

    protected Integer getIntegerOrRandom(String paramValue, String fieldName) {
        if (Objects.equals(paramValue, wildCard)) {
            return randomInteger();
        }
        try {
            return Integer.parseInt(paramValue);
        } catch (Exception cause) {
            throw new ValidationException(String.format(INVALID_REQUIRED_PARAM.getValue(),
                    fieldName, getParameterizeClassName()));
        }
    }

    protected Float getFloatOrRandom(String paramValue, String fieldName) {
        if (Objects.equals(paramValue, wildCard)) {
            return randomFloat();
        }
        try {
            return Float.parseFloat(paramValue);
        } catch (Exception cause) {
            throw new ValidationException(String.format(INVALID_REQUIRED_PARAM.getValue(),
                    fieldName, getParameterizeClassName()));
        }
    }

    protected <M extends OcpiRequestData> List<M> parseToListOrGenerateRandom(String paramValue, String fieldName,
                                                                              Class<M> clazz, Supplier<M> supplier) {
        if (Objects.equals(paramValue, wildCard)) {
            return Generators.generateList(supplier);
        }
        return List.of(parseModelsFromJson(paramValue, fieldName, clazz));
    }

    protected <E extends Enum<E>> List<E> parseListOfEnums(String value, String fieldName,
                                                           Class<E> clazz) {
        E[] result;
        try {
            log.info("JSON string for array parsing: " + value);
            result = mapper.readerForArrayOf(clazz).readValue(value);
            log.info("Models parsed from string: " + Arrays.toString(result));
        } catch (JsonProcessingException e) {
            throw new ValidationException(
                    String.format(INVALID_FIELD_VALUE.getValue(), getParameterizeClassName(), fieldName, value));
        }
        return List.of(result);
    }

    protected String getStringOrRandom(String paramValue, int length) {
        if (Objects.equals(paramValue, wildCard)) {
            return Generators.generateString(length);
        }
        return paramValue;
    }

    protected Boolean parseBooleanOrRandom(String paramValue) {
        if (Objects.equals(paramValue, wildCard)) {
            return randomBoolean();
        }
        return Boolean.valueOf(paramValue);
    }

    protected <E extends Enum<E>> E parseEnumOrRandom(String paramValue, String fieldName, Class<E> clazz) {
        if (Objects.equals(paramValue, wildCard)) {
            return randomEnum(clazz);
        }
        return parseEnum(paramValue, fieldName, clazz);
    }

    protected <E extends Enum<E>> E parseEnum(String value,
                                              String fieldName, Class<E> clazz) {
        for (E en : EnumSet.allOf(clazz)) {
            if (Objects.equals(en.name(), value)) {
                return en;
            }
        }
        throw new ValidationException(String.format(INVALID_REQUIRED_PARAM.getValue(),
                fieldName, getParameterizeClassName()));
    }

    protected <M extends OcpiRequestData> M parseObjectOrRandom(String paramValue,
                                                                String fieldName,
                                                                Class<M> clazz,
                                                                Supplier<M> supplier) {
        if (Objects.equals(paramValue, wildCard)) {
            return supplier.get();
        }
        return parseModelFromJson(paramValue, fieldName, clazz);
    }

    protected <M extends OcpiRequestData> M parseModelFromJson(String value, String fieldName, Class<M> clazz) {
        try {
            log.info("JSON string for parsing: " + value);
            M model = mapper.readValue(value, clazz);
            log.info("Model parsed from string: " + model);
            return model;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new ValidationException(
                    String.format(INVALID_FIELD_VALUE.getValue(), getParameterizeClassName(), fieldName, value));
        }
    }

    protected <M extends OcpiRequestData> M[] parseModelsFromJson(String value, String fieldName, Class<M> clazz) {
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

}
