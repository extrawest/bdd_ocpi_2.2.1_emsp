package com.extrawest.bdd_cpo_ocpi.utils;

import com.extrawest.ocpi.model.OcpiRequestData;
import com.extrawest.ocpi.model.dto.PriceComponentDTO;
import com.extrawest.ocpi.model.dto.TariffElementDTO;
import com.extrawest.ocpi.model.enums.*;
import com.extrawest.ocpi.model.vo.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class Generators {
    public static String generateString(int length) {
        return RandomStringUtils.random(length, true, false);
    }

    public static <T extends OcpiRequestData> List<T> generateList(Supplier<T> supplier) {
        int length = randomInteger(2, 4);

        return IntStream.range(1, length)
                .mapToObj((i) -> supplier.get())
                .collect(Collectors.toList());
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = ThreadLocalRandom.current().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static Boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    public static LocalDateTime randomDateTime() {
        LocalDate localDate = randomDate();
        LocalTime localTime = randomTime();
        return LocalDateTime.of(localDate, localTime);
    }

    private static LocalTime randomTime() {
        int randomTime = ThreadLocalRandom.current()
                .nextInt(LocalTime.MIN.toSecondOfDay(), LocalTime.MAX.toSecondOfDay());
        return LocalTime.ofSecondOfDay(randomTime);
    }

    @NotNull
    private static LocalDate randomDate() {
        LocalDate endExclusive = LocalDate.now();
        LocalDate startInclusive = endExclusive.minusMonths(36);
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom.current()
                .nextLong(startEpochDay, endEpochDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public float randomFloat() {
        float leftLimit = 1F;
        float rightLimit = 500F;
        Float randomFloat = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);
        return Float.parseFloat(String.format("%.4f", randomFloat));
    }

    private Integer randomInteger(int leftLimit, int rightLimit) {
        return leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
    }

    public Integer randomInteger() {
        int leftLimit = 1;
        int rightLimit = 250;
        return randomInteger(leftLimit, rightLimit);
    }

    public static TariffElementDTO generateTariffElement() {
        TariffElementDTO element = new TariffElementDTO();
        List<PriceComponentDTO> components = new ArrayList<>();
        PriceComponentDTO priceComponentDTO = generatePriceComponent();
        components.add(priceComponentDTO);
        element.setPriceComponents(components);
        return element;
    }

    private static PriceComponentDTO generatePriceComponent() {
        PriceComponentDTO component = new PriceComponentDTO();
        component.setType(randomEnum(TariffDimensionType.class));
        component.setPrice(randomFloat());
        return component;
    }

    public static CdrToken generateCdrToken() {
        CdrToken cdrToken = new CdrToken();
        cdrToken.setCountryCode(generateString(2));
        cdrToken.setPartyId(generateString(3));
        cdrToken.setUid(generateString(36));
        cdrToken.setType(randomEnum(TokenType.class));
        cdrToken.setContractId(generateString(36));
        return cdrToken;
    }

    public static CdrLocation generateCdrLocation() {
        CdrLocation cdrLocation = new CdrLocation();
        cdrLocation.setLocationId(generateString(36));
        cdrLocation.setAddress(generateString(45));
        cdrLocation.setCity(generateString(45));
        cdrLocation.setCountry(generateString(3));
        cdrLocation.setCoordinates(generateGeoLocation());
        cdrLocation.setEvseUid(generateString(36));
        cdrLocation.setEvseId(generateString(48));
        cdrLocation.setConnectorId(generateString(36));
        return cdrLocation;
    }

    public static ChargingPeriod generateChargingPeriod() {
        ChargingPeriod chargingPeriod = new ChargingPeriod();
        chargingPeriod.setStartDateTime(randomDateTime());
        chargingPeriod.setDimensions(generateList(Generators::generateCdrDimension));
        return chargingPeriod;
    }

    public static CdrDimension generateCdrDimension() {
        CdrDimension cdrDimension = new CdrDimension();
        cdrDimension.setType(randomEnum(CdrDimensionType.class));
        cdrDimension.setVolume(randomFloat());
        return cdrDimension;
    }

    public static Price generatePrice() {
        Price price = new Price();
        price.setExclVat(randomFloat());
        price.setInclVat(randomFloat());
        return price;
    }

    public static GeoLocation generateGeoLocation() {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(generateString(10));
        geoLocation.setLongitude(generateString(11));
        return geoLocation;
    }

    public static Connector generateConnector() {
        Connector connector = new Connector();
        connector.setConnectorId(generateString(36));
        connector.setLastUpdated(randomDateTime());
        connector.setFormat(randomEnum(ConnectorFormat.class));
        connector.setMaxAmperage(randomInteger());
        connector.setStandard(randomEnum(ConnectorType.class));
        connector.setTariffIds(List.of(generateString(36)));
        connector.setMaxVoltage(randomInteger());
        connector.setMaxElectricPower(randomInteger());
        connector.setPowerType(randomEnum(PowerType.class));
        return connector;
    }

    public static CredentialsRole generateCredentialsRole() {
        CredentialsRole role = new CredentialsRole();
        role.setRole(randomEnum(Role.class));
        role.setCountryCode(generateString(2));
        role.setPartyId(generateString(3));
        role.setBusinessDetails(generateBusinessDetails());
        return role;
    }

    public static BusinessDetails generateBusinessDetails() {
        BusinessDetails businessDetails = new BusinessDetails();
        businessDetails.setName(generateString(36));
        return businessDetails;
    }
}

