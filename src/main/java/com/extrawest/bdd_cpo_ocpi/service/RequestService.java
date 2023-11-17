package com.extrawest.bdd_cpo_ocpi.service;

import io.restassured.response.Response;

public interface RequestService {
    Response sendRequest(RequestHolder holder);
}
