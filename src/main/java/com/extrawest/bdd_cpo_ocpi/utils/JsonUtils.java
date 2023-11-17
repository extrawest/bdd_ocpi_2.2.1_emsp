package com.extrawest.bdd_cpo_ocpi.utils;

import com.extrawest.bdd_cpo_ocpi.exception.ApiErrorMessage;
import com.extrawest.bdd_cpo_ocpi.exception.BddTestingException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class JsonUtils {
    public static String readJson(String fileName) {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(fileName);
        if (inputStream == null) {
            log.error("File {} was not found", fileName);
            throw new BddTestingException(String.format(ApiErrorMessage.FILE_NOT_FOUND.getValue(), fileName));
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader.lines().collect(Collectors.joining());
    }

}
