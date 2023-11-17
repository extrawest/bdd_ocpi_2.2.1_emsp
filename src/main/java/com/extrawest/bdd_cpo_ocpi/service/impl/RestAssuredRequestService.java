package com.extrawest.bdd_cpo_ocpi.service.impl;

import com.extrawest.bdd_cpo_ocpi.exception.BddTestingException;
import com.extrawest.bdd_cpo_ocpi.service.RequestService;
import com.extrawest.bdd_cpo_ocpi.service.RequestHolder;
import io.cucumber.spring.ScenarioScope;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

import static com.extrawest.bdd_cpo_ocpi.exception.ApiErrorMessage.FAILED_TO_REQUEST;
import static com.extrawest.bdd_cpo_ocpi.exception.ApiErrorMessage.UNKNOWN_HTTP_METHOD;
import static io.restassured.RestAssured.given;

@ScenarioScope(proxyMode = ScopedProxyMode.NO)
@Component
@RequiredArgsConstructor
public class RestAssuredRequestService implements RequestService {

    @Override
    public Response sendRequest(RequestHolder holder) {
        RequestSpecification requestSpecification = setupRequestSpecification(holder);

        return Optional.of(getRequestSpecificationExecutorFor(requestSpecification,
                        holder.getHttpMethod(),
                        holder.getRequestAddress()))
                .map(Supplier::get)
                .orElseThrow(() -> new BddTestingException(FAILED_TO_REQUEST.getValue()));
    }

    private RequestSpecification setupRequestSpecification(RequestHolder holder) {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RequestSpecification requestSpecification = given()
                .headers(holder.getHeaders())
                .pathParams(holder.getPathParams())
                .queryParams(holder.getQueryParams());

        if (holder.getBody() != null) {
            requestSpecification.body(holder.getBody());
        }
        return requestSpecification;
    }

    private Supplier<Response> getRequestSpecificationExecutorFor(RequestSpecification requestSpecification,
                                                                  Method httpMethod,
                                                                  String requestAddress) {
        Supplier<Response> requestSpecificationExecutor;
        switch (httpMethod) {
            case GET -> requestSpecificationExecutor = () -> requestSpecification.get(requestAddress);
            case HEAD -> requestSpecificationExecutor = () -> requestSpecification.head(requestAddress);
            case POST -> requestSpecificationExecutor = () -> requestSpecification.post(requestAddress);
            case PUT -> requestSpecificationExecutor = () -> requestSpecification.put(requestAddress);
            case PATCH -> requestSpecificationExecutor = () -> requestSpecification.patch(requestAddress);
            case DELETE -> requestSpecificationExecutor = () -> requestSpecification.delete(requestAddress);
            case OPTIONS -> requestSpecificationExecutor = () -> requestSpecification.options(requestAddress);
            default -> throw new BddTestingException(String.format(UNKNOWN_HTTP_METHOD.getValue(), httpMethod));
        }
        return requestSpecificationExecutor;
    }
}
