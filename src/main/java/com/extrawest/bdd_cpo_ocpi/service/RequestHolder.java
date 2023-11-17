package com.extrawest.bdd_cpo_ocpi.service;

import com.extrawest.ocpi.model.OcpiRequestData;
import io.cucumber.spring.ScenarioScope;
import io.restassured.http.Method;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ScenarioScope(proxyMode = ScopedProxyMode.NO)
@RequiredArgsConstructor
@Getter
public class RequestHolder {
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();
    private final Map<String, String> pathParams = new HashMap<>();
    @Setter
    private Method httpMethod;
    @Setter
    private String requestAddress;

    private OcpiRequestData body;

    public void addHeaders(Map<String, String> requestHeaders) {
        addNewAndReplaceExisted(headers, requestHeaders);
    }

    public void addQueryParameters(Map<String, String> params) {
        addNewAndReplaceExisted(queryParams, params);
    }

    public void addPathParameters(Map<String, String> params) {
        addNewAndReplaceExisted(pathParams, params);
    }

    private static void addNewAndReplaceExisted(Map<String, String> existed, Map<String, String> additional) {
        existed.putAll(
                additional.entrySet().stream()
                        .filter(entry -> !existed.containsKey(entry.getKey())
                                || !existed.get(entry.getKey()).equals(entry.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    public <T extends OcpiRequestData> void setBody(T body) {
        this.body = body;
    }
}
