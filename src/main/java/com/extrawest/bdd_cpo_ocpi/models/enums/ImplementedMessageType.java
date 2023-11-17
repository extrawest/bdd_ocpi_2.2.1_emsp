package com.extrawest.bdd_cpo_ocpi.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

import static com.extrawest.bdd_cpo_ocpi.utils.EnumUtils.findByField;

@Getter
@AllArgsConstructor
public enum ImplementedMessageType {
    TARIFF("tariff"),
    VERSION("version"),
    VERSION_DETAILS("version details"),
    SESSION("session"),
    CDR("cdr"),
    LOCATION("location"),
    EVSE("evse"),
    CONNECTOR("connector"),
    TOKENS("tokens"),
    AUTHORIZATION_INFO("authorization info"),
    AUTHORIZE("authorize"),
    COMMAND("command"),
    CREDENTIALS("credentials");

    private final String value;

    public static boolean contains(String name) {
        return Arrays.stream(ImplementedMessageType.values())
                .anyMatch(e -> Objects.equals(e.getValue(), name));
    }

    public static ImplementedMessageType fromValue(String value) {
        return findByField(
                ImplementedMessageType.class,
                ImplementedMessageType::getValue,
                value);
    }

    @Override
    public String toString() {
        return this.value;
    }

}
