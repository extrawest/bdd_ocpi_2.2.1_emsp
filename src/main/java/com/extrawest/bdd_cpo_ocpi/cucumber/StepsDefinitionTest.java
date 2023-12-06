package com.extrawest.bdd_cpo_ocpi.cucumber;

import com.extrawest.bdd_cpo_ocpi.ServerEndpoints;
import com.extrawest.bdd_cpo_ocpi.config.CpoConfig;
import com.extrawest.bdd_cpo_ocpi.config.EmspConfig;
import com.extrawest.bdd_cpo_ocpi.containers.ContainerBase;
import com.extrawest.bdd_cpo_ocpi.exception.BddTestingException;
import com.extrawest.bdd_cpo_ocpi.models.enums.ImplementedMessageType;
import com.extrawest.bdd_cpo_ocpi.repository.CredentialsRepository;
import com.extrawest.bdd_cpo_ocpi.repository.VersionDetailsRepository;
import com.extrawest.bdd_cpo_ocpi.repository.VersionsRepository;
import com.extrawest.bdd_cpo_ocpi.service.MessageService;
import com.extrawest.bdd_cpo_ocpi.service.RequestHolder;
import com.extrawest.bdd_cpo_ocpi.service.RequestService;
import com.extrawest.bdd_cpo_ocpi.service.impl.ResponseParsingService;
import com.extrawest.bdd_cpo_ocpi.utils.RepositoryUtils;
import com.extrawest.ocpi.model.dto.*;
import com.extrawest.ocpi.model.dto.token.TokenDto;
import com.extrawest.ocpi.model.enums.Role;
import com.extrawest.ocpi.model.enums.status_codes.OcpiStatusCode;
import com.extrawest.ocpi.model.markers.OcpiRequestData;
import com.extrawest.ocpi.model.markers.OcpiResponseData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.http.Method;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.CodecRegistryProvider;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.extrawest.bdd_cpo_ocpi.exception.ApiErrorMessage.EMPTY_EXPECTED_VALUE;
import static com.extrawest.bdd_cpo_ocpi.utils.JsonUtils.readJson;
import static com.extrawest.bdd_cpo_ocpi.utils.RepositoryUtils.importToCollection;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Slf4j
@RequiredArgsConstructor
@CucumberContextConfiguration
@SpringBootTest
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources")
@Testcontainers
public class StepsDefinitionTest extends ContainerBase {
    private static Response response;

    private final VersionDetailsRepository versionDetailsRepository;
    private final VersionsRepository versionsRepository;
    private final CredentialsRepository credentialsRepository;

    private final ServerEndpoints serverEndpoints;
    private final MessageService messagingService;
    private final ResponseParsingService responseListService;

    private final RequestService httpStepsService;
    private final RequestHolder requestHolder;

    private final EmspConfig emspConfig;
    private final CpoConfig cpoConfig;

    private final MongoTemplate mongoTemplate;

    private final static String AUTHORIZATION_TOKEN = "Token %s";
    private final static String STATUS_CODE = "status_code";

    @Autowired
    @Qualifier("mongoDatabaseFactory")
    private CodecRegistryProvider codecRegistryProvider;

    private static final List<String> mongoCollections = new ArrayList<>();
    private int stepNumber;


    private static int getOcpiStatusCode(Response response) {
        return response.jsonPath().getInt(STATUS_CODE);
    }

    @Before
    public void scenarioIncrease(Scenario scenario) {
        int scenarioLine = scenario.getLine();
        String scenarioName = scenario.getName();

        String scenarioFileName = Paths.get(scenario.getUri()).getFileName().toString();
        log.info(String.format("\nNew Scenario: %s (%s, line %s)", scenarioName, scenarioFileName, scenarioLine));

        stepNumber = 0;

        requestHolder.addHeaders(Map.of(
                HttpHeaders.CONTENT_TYPE, APPLICATION_JSON.toString(),
                HttpHeaders.AUTHORIZATION, String.format(AUTHORIZATION_TOKEN, emspConfig.getTokenA())
        ));

        getVersions();
        getVersionDetails();
    }

    @BeforeStep
    public void stepIncrease() {
        stepNumber++;
        requestHolder.setBody(null);
    }

    @After
    public void tearDown() {
        mongoCollections.forEach(mongoCollection -> RepositoryUtils.remove(mongoTemplate,
                new org.springframework.data.mongodb.core.query.Query(),
                mongoCollection));
        mongoCollections.clear();

        versionsRepository.clear();
        versionDetailsRepository.clear();
    }

    @Given("eMSP has {string} data {string}")
    public void createMongoCollectionWithData(String collectionName, String filePath) {
        mongoCollections.add(collectionName);
        importToCollection(codecRegistryProvider, mongoTemplate,
                readJson(filePath), collectionName);
        log.info("STEP {}: added {} into DB collection {}", stepNumber, filePath, collectionName);
    }

    @Given("CPO is registered in eMSP system")
    public void registerCpo() {
        CredentialsDto credentials = getCredentials();
        requestHolder.addHeaders(Map.of(
                HttpHeaders.AUTHORIZATION, String.format(AUTHORIZATION_TOKEN, credentials.getToken()))
        );
    }

    ////////////////////////////////////////////////    Query params   /////////////////////////////////////////////////
    @And("{string} query param is {string}")
    public void addQueryParams(String parameterName, String parameterValue) {
        Map<String, String> requestParameters = Map.of(parameterName, parameterValue);
        requestHolder.addQueryParameters(requestParameters);
    }

    @And("{string} path param is {string}")
    public void addPathParams(String parameterName, String parameterValue) {
        Map<String, String> pathParameters = Map.of(parameterName, parameterValue);
        requestHolder.addPathParameters(pathParameters);
    }

    /////////////////////////////////////////////////        Put      //////////////////////////////////////////////////
    @Given("CPO with {string} {string} and {string} {string} put {string} with {string} {string} in eMSP system with data")
    public void put(String paramName1, String paramValue1,
                    String paramName2, String paramValue2,
                    String typeName,
                    String paramName3, String paramValue3,
                    DataTable body) {
        ImplementedMessageType implementedType = getImplementedMessageType(typeName);

        Map<String, String> parameters = Map.of(
                paramName1, paramValue1,
                paramName2, paramValue2,
                paramName3, paramValue3
        );
        requestHolder.addQueryParameters(parameters);

        if (nonNull(body) && !body.isEmpty()) {
            OcpiRequestData ocpiData = messagingService.createRequestBody(implementedType, body.asMap());
            requestHolder.setBody(ocpiData);
        }

        response = sendRequest(Method.PUT, implementedType);
        log.info("STEP {}: CPO send PUT {} into eMSP system", stepNumber, typeName);
    }

    @Given("CPO with {string} {string} and {string} {string} put {string} in eMSP system with data")
    public void put(String paramName1, String paramValue1,
                    String paramName2, String paramValue2,
                    String typeName, DataTable body) {

        ImplementedMessageType implementedType = getImplementedMessageType(typeName);
        Map<String, String> parameters = Map.of(paramName1, paramValue1, paramName2, paramValue2);
        requestHolder.addQueryParameters(parameters);

        if (nonNull(body) && !body.isEmpty()) {
            OcpiRequestData ocpiData = messagingService.createRequestBody(implementedType, body.asMap());
            requestHolder.setBody(ocpiData);
        }
        response = sendRequest(Method.PUT, implementedType);
        log.info("STEP {}: CPO send PUT {} into eMSP system", stepNumber, typeName);
    }

    @Given("CPO with {string} {string} put {string} in eMSP system with data")
    public void put(String paramName1, String paramValue1,
                    String typeName,
                    DataTable body) {
        ImplementedMessageType implementedType = getImplementedMessageType(typeName);
        Map<String, String> parameters = Map.of(paramName1, paramValue1);
        requestHolder.addQueryParameters(parameters);

        if (nonNull(body) && !body.isEmpty()) {
            OcpiRequestData ocpiData = messagingService.createRequestBody(implementedType, body.asMap());
            requestHolder.setBody(ocpiData);
        }
        response = sendRequest(Method.PUT, implementedType);
        log.info("STEP {}: CPO send PUT {} into eMSP system", stepNumber, typeName);
    }

    @When("CPO updates {string}")
    public void put(String messageType, DataTable body) {
        ImplementedMessageType implementedType = getImplementedMessageType(messageType);
        if (nonNull(body) && !body.isEmpty()) {
            OcpiRequestData ocpiData = messagingService.createRequestBody(implementedType, body.asMap());
            requestHolder.setBody(ocpiData);
        }
        response = sendRequest(Method.PUT, implementedType);
        log.info("STEP {}: CPO updates {} in eMSP system", stepNumber, messageType);
    }

    /////////////////////////////////////////////////        Post      /////////////////////////////////////////////////
    @Given("CPO post {string} with {string} {string} in eMSP system with data")
    public void post(String messageType,
                     String paramName1, String paramValue1,
                     DataTable body) {
        ImplementedMessageType implementedType = getImplementedMessageType(messageType);
        Map<String, String> parameters = Map.of(paramName1, paramValue1);
        requestHolder.addQueryParameters(parameters);

        if (nonNull(body) && !body.isEmpty()) {
            OcpiRequestData ocpiData = messagingService.createRequestBody(implementedType, body.asMap());
            requestHolder.setBody(ocpiData);
        }
        response = sendRequest(Method.POST, implementedType);
        log.info("STEP {}: CPO send PUT {} into eMSP system", stepNumber, messageType);
    }

    @Given("CPO post {string} in eMSP system with data")
    public void post(String messageType, DataTable body) {
        ImplementedMessageType implementedType = getImplementedMessageType(messageType);

        if (nonNull(body) && !body.isEmpty()) {
            OcpiRequestData ocpiData = messagingService.createRequestBody(implementedType, body.asMap());
            requestHolder.setBody(ocpiData);
        }
        response = sendRequest(Method.POST, implementedType);
        log.info("STEP {}: CPO send PUT {} into eMSP system", stepNumber, messageType);
    }

    public CredentialsDto getCredentials() {
        CredentialsDto cpoCredentials = new CredentialsDto();
        cpoCredentials.setUrl(cpoConfig.getVersionUrl());
        cpoCredentials.setToken(cpoConfig.getTokenA());
        CredentialsRole credentialsRole = CredentialsRole.builder()
                .role(Role.valueOf(cpoConfig.getRole()))
                .businessDetails(new BusinessDetails())
                .countryCode(cpoConfig.getCountryCode())
                .partyId(cpoConfig.getPartyId())
                .build();
        cpoCredentials.setRoles(List.of(credentialsRole));

        requestHolder.setBody(cpoCredentials);

        ImplementedMessageType implementedType = ImplementedMessageType.CREDENTIALS;
        response = sendRequest(Method.POST, implementedType);

        checkResponseIsSuccess();

        CredentialsDto emspCredentials = (CredentialsDto)
                messagingService.createResponseBody(implementedType, response);

        credentialsRepository.setEmspCredentials(emspCredentials);

        log.info("Before tests: CPO received emsp credentials");
        return emspCredentials;
    }

    /////////////////////////////////////////////////      Delete      /////////////////////////////////////////////////
    @When("CPO with {string} {string} and {string} {string} removes his {string} with {string} {string}")
    public void delete(String paramName1, String paramValue1,
                       String paramName2, String paramValue2,
                       String messageType,
                       String paramName3, String paramValue3) {
        ImplementedMessageType implementedType = getImplementedMessageType(messageType);
        Map<String, String> parameters = Map.of(
                paramName1, paramValue1,
                paramName2, paramValue2,
                paramName3, paramValue3);
        requestHolder.addQueryParameters(parameters);

        response = sendRequest(Method.DELETE, implementedType);
        log.info("STEP {}: CPO removes {} from eMSP system", stepNumber, messageType);
    }

    @When("CPO removes his {string}")
    public void delete(String messageType) {
        ImplementedMessageType implementedType = getImplementedMessageType(messageType);
        response = sendRequest(Method.DELETE, implementedType);

        log.info("STEP {}: CPO removes his {} in eMSP system", stepNumber, messageType);
    }

    /////////////////////////////////////////////////      Get      ////////////////////////////////////////////////////
    @When("CPO with {string} {string} and {string} {string} checks {string} with {string} {string} in eMSP system")
    public void get(String paramName1, String paramValue1,
                    String paramName2, String paramValue2,
                    String messageType,
                    String paramName3, String paramValue3) {
        ImplementedMessageType implementedType = getImplementedMessageType(messageType);
        Map<String, String> parameters = Map.of(
                paramName1, paramValue1,
                paramName2, paramValue2,
                paramName3, paramValue3);
        requestHolder.addQueryParameters(parameters);

        response = sendRequest(Method.GET, implementedType);

        log.info("STEP {}: CPO gets his {} from eMSP system, {} exists", stepNumber, messageType, messageType);
    }

    @When("CPO checks {string} with {string} {string} in eMSP system")
    public void get(String messageType, String paramName, String paramValue) {
        ImplementedMessageType implementedType = getImplementedMessageType(messageType);
        Map<String, String> parameters = Map.of(paramName, paramValue);
        requestHolder.addQueryParameters(parameters);

        response = sendRequest(Method.GET, implementedType);
        log.info("STEP {}: CPO checks his {} in eMSP system, {} {}={} exists",
                stepNumber, messageType, messageType, paramName, paramValue);
    }

    public void getVersions() {
        createMongoCollectionWithData("tokens-a", "db/credentials.json");
        ImplementedMessageType implementedType = ImplementedMessageType.VERSION;
        response = sendRequest(Method.GET, implementedType);
        checkResponseIsSuccess();

        List<VersionDto> versionsList = ResponseParsingService.parseList(response, VersionDto.class);
        versionsRepository.addAll(versionsList);

        log.info("Before tests: CPO received list of versions from eMSP");
    }

    public void getVersionDetails() {
        ImplementedMessageType implementedType = ImplementedMessageType.VERSION_DETAILS;
        response = sendRequest(Method.GET, implementedType);
        checkResponseIsSuccess();

        VersionDetailsDto details = (VersionDetailsDto)
                messagingService.createResponseBody(implementedType, response);
        versionDetailsRepository.addAll(details);

        log.info("Before tests: CPO received 2.2.1 version details from eMSP");
    }

    @When("CPO checks {string} in eMSP system")
    public void get(String messageType) {
        ImplementedMessageType implementedType = getImplementedMessageType(messageType);
        response = sendRequest(Method.GET, implementedType);

        log.info("STEP {}: CPO checks his {} in eMSP system, {} exists", stepNumber, messageType, messageType);
    }

    /////////////////////////////////////////////////  Get and check  //////////////////////////////////////////////////
    @Then("{string} is absent")
    public void getAndCheckNotFound(String messageType) {
        ImplementedMessageType implementedType = getImplementedMessageType(messageType);

        response = sendRequest(Method.GET, implementedType);
        checkHttpResponseStatusCode(HttpStatus.NOT_FOUND.value());
        checkOcpiResponseStatusCode(OcpiStatusCode.CLIENT_ERROR.getValue());
        log.info("STEP {}: {} is absent", stepNumber, messageType);
    }

    /////////////////////////////////////////////////    Check response   //////////////////////////////////////////////
    @And("response is success")
    public void checkResponseIsSuccess() {
        checkResponseIsSuccess(response);
        log.info("STEP {}: response is success", stepNumber);
    }

    @Then("eMSP responded with HTTP status {int}")
    public void checkHttpResponseStatusCode(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.statusCode());
        log.info("STEP {}: eMSP responded with HTTP status {}", stepNumber, expectedStatusCode);
    }

    @Then("eMSP responded with OCPI status {int}")
    public void checkOcpiResponseStatusCode(int expectedOcpiStatusCode) {
        int actualOcpiStatusCode = getOcpiStatusCode(response);
        assertEquals(expectedOcpiStatusCode, actualOcpiStatusCode);

        log.info("STEP {}: eMSP responded with OCPI status {}", stepNumber, expectedOcpiStatusCode);
    }

    @Then("{string} response is valid and has data")
    public void validateResponse(String messageType, DataTable table) {
        checkResponseIsSuccess(response);
        Map<String, String> parameters = isNull(table) || table.isEmpty() ? Collections.emptyMap() : table.asMap();

        ImplementedMessageType type = getImplementedMessageType(messageType);
        OcpiResponseData responseBody = messagingService.createResponseBody(type, response);
        messagingService.validateResponseBody(parameters, responseBody);

        log.info("STEP {}: Response is valid and fields are asserted", stepNumber);
    }

    @And("list of versions response is valid and contains values")
    public void validateListResponseContains(DataTable table) {
        checkResponseIsSuccess(response);
        if (isNull(table) || table.isEmpty()) {
            log.info("STEP {}: Expected values was not provided", stepNumber);
            throw new BddTestingException(EMPTY_EXPECTED_VALUE.getValue());
        }
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        responseListService.validateResponseListContains(rows, response, VersionDto.class);
        log.info("STEP {}: Response is valid and fields are asserted", stepNumber);
    }

    @And("list of tokens response is valid and is")
    public void validateListResponseIs(DataTable table) {
        checkResponseIsSuccess(response);
        if (isNull(table) || table.isEmpty()) {
            log.info("STEP {}: Expected values was not provided", stepNumber);
            throw new BddTestingException(EMPTY_EXPECTED_VALUE.getValue());
        }
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        responseListService.validateResponseListEquals(rows, response, TokenDto.class, ImplementedMessageType.TOKENS);
        log.info("STEP {}: Response is valid and fields are asserted", stepNumber);
    }

    private Response sendRequest(Method httpMethod, ImplementedMessageType messageType) {
        String url = serverEndpoints.getUrl(messageType);
        requestHolder.setRequestAddress(url);
        requestHolder.setHttpMethod(httpMethod);

        try {
            return httpStepsService.sendRequest(requestHolder);
        } catch (Exception e) {
            throw new BddTestingException(String.format("STEP %s: server responded with error: %s",
                    stepNumber, e.getMessage()));
        }
    }

    private ImplementedMessageType getImplementedMessageType(String messageType) {
        if (ImplementedMessageType.contains(messageType)) {
            return ImplementedMessageType.fromValue(messageType);
        } else {
            throw new BddTestingException(
                    String.format("STEP %s: wrong message request type or %s is not implemented.",
                            stepNumber, messageType));
        }
    }

    private void checkResponseIsSuccess(Response response) {
        if (!HttpStatusCode.valueOf(response.statusCode()).is2xxSuccessful()) {
            throw new BddTestingException(String.format("STEP %s: Server responded with http status code %s: %s",
                    stepNumber,
                    response.getStatusCode(),
                    response.asPrettyString()));
        }
    }
}