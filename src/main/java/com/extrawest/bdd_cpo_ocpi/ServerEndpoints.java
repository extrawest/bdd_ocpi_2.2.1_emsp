package com.extrawest.bdd_cpo_ocpi;

import com.extrawest.bdd_cpo_ocpi.config.EmspConfig;
import com.extrawest.bdd_cpo_ocpi.models.enums.ImplementedMessageType;
import com.extrawest.bdd_cpo_ocpi.repository.VersionDetailsRepository;
import com.extrawest.bdd_cpo_ocpi.repository.VersionsRepository;
import com.extrawest.ocpi.model.enums.ModuleID;
import com.extrawest.ocpi.model.enums.VersionNumber;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@AllArgsConstructor
@Component
public class ServerEndpoints {
    @Autowired
    private final VersionDetailsRepository versionDetailsRepository;
    @Autowired
    private final VersionsRepository versionsRepository;
    public Map<ImplementedMessageType, Supplier<String>> endpoints;
    @Autowired
    private EmspConfig emspConfig;

    public String getUrl(ImplementedMessageType messageType) {
        if (messageType.equals(ImplementedMessageType.VERSION)) {
            return emspConfig.getVersionUrl();
        } else if (endpoints.isEmpty()) {
            this.endpoints = configEndpoints();
        }
        return endpoints.get(messageType).get();
    }

    private Map<ImplementedMessageType, Supplier<String>> configEndpoints() {
        Map<ImplementedMessageType, Supplier<String>> urls = new HashMap<>();

        urls.put(ImplementedMessageType.TARIFF, () -> versionDetailsRepository.getEndpoint(ModuleID.TARIFFS).getUrl());
        urls.put(ImplementedMessageType.SESSION, () -> versionDetailsRepository.getEndpoint(ModuleID.SESSIONS).getUrl());
        urls.put(ImplementedMessageType.CDR, () -> versionDetailsRepository.getEndpoint(ModuleID.CDRS).getUrl());
        urls.put(ImplementedMessageType.LOCATION, () -> versionDetailsRepository.getEndpoint(ModuleID.LOCATIONS).getUrl()
                + "/{country_code}/{party_id}/{location_id}");
        urls.put(ImplementedMessageType.EVSE, () -> versionDetailsRepository.getEndpoint(ModuleID.LOCATIONS).getUrl()
                + "/{country_code}/{party_id}/{location_id}/{evse_uid}");
        urls.put(ImplementedMessageType.CONNECTOR,
                () -> versionDetailsRepository.getEndpoint(ModuleID.LOCATIONS).getUrl()
                + "/{country_code}/{party_id}/{location_id}/{evse_uid}/{connector_id}");
        urls.put(ImplementedMessageType.TOKENS, () -> versionDetailsRepository.getEndpoint(ModuleID.TOKENS).getUrl());
        urls.put(ImplementedMessageType.AUTHORIZE, () ->
                String.format("%s%s", versionDetailsRepository.getEndpoint(ModuleID.TOKENS).getUrl(),
                        "/{token_uid}/authorize"));
        urls.put(ImplementedMessageType.COMMAND, () -> versionDetailsRepository.getEndpoint(ModuleID.COMMANDS).getUrl()
                + "/{command_type}/{unique_id}");
        urls.put(ImplementedMessageType.VERSION_DETAILS,
                () -> versionsRepository.getVersionDetailsUrl(VersionNumber.V_2_2_1));
        urls.put(ImplementedMessageType.CREDENTIALS,
                () -> versionDetailsRepository.getEndpoint(ModuleID.CREDENTIALS).getUrl());

        return urls;
    }

}

