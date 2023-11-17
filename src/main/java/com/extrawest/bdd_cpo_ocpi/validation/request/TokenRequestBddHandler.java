package com.extrawest.bdd_cpo_ocpi.validation.request;

import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
import com.extrawest.ocpi.model.dto.TokenDTO;
import com.extrawest.ocpi.model.enums.ProfileType;
import com.extrawest.ocpi.model.enums.TokenType;
import com.extrawest.ocpi.model.enums.WhitelistType;
import com.extrawest.ocpi.model.vo.EnergyContract;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenRequestBddHandler extends OutgoingMessageFieldsFactory<TokenDTO>
        implements RequestMessageFactory<TokenDTO> {
    public static final String COUNTRY_CODE_REQUIRED = "country_code";
    public static final String PARTY_ID_REQUIRED = "party_id";
    public static final String UID_REQUIRED = "uid";
    public static final String TYPE_REQUIRED = "type";
    public static final String CONTRACT_ID_REQUIRED = "contract_id";
    public static final String ISSUER_REQUIRED = "issuer";
    public static final String VALID_REQUIRED = "valid";
    public static final String WHITELIST_REQUIRED = "whitelist";
    public static final String LAST_UPDATED_REQUIRED = "last_updated";

    public static final String VISUAL_NUMBER = "visual_number";
    public static final String GROUP_ID = "group_id";
    public static final String LANGUAGE = "language";
    public static final String DEFAULT_PROFILE_TYPE = "default_profile_type";
    public static final String ENERGY_CONTRACT = "energy_contract";

    @PostConstruct
    private void init() {
        this.requiredFieldsSetup = Map.of(
                COUNTRY_CODE_REQUIRED, (req, countryCode) -> req.setCountryCode(
                        getStringOrRandom(countryCode, 2)),
                PARTY_ID_REQUIRED, (req, partyId) -> req.setPartyId(
                        getStringOrRandom(partyId, 3)),
                UID_REQUIRED, (req, id) -> req.setUid(getStringOrRandom(id, 36)),
                TYPE_REQUIRED, (req, currency) -> req.setType(
                        parseEnumOrRandom(currency, TYPE_REQUIRED, TokenType.class)),
                CONTRACT_ID_REQUIRED, (req, contractId) ->
                        req.setContractId(getStringOrRandom(contractId, 36)),
                ISSUER_REQUIRED, (req, issuer) ->
                        req.setIssuer(getStringOrRandom(issuer, 64)),
                VALID_REQUIRED, (req, valid) -> req.setValid(parseBooleanOrRandom(valid)),
                WHITELIST_REQUIRED, (req, whitelist) -> req.setWhitelist(
                        parseEnumOrRandom(whitelist, WHITELIST_REQUIRED, WhitelistType.class)),
                LAST_UPDATED_REQUIRED, (req, lastUpdated) -> req.setLastUpdated(
                        parseLocalDateTimeOrGenerateRandom(lastUpdated, LAST_UPDATED_REQUIRED)));

        this.optionalFieldsSetup = new HashMap<>();
        this.optionalFieldsSetup.put(VISUAL_NUMBER, TokenDTO::setVisualNumber);
        this.optionalFieldsSetup.put(GROUP_ID, TokenDTO::setGroupId);
        this.optionalFieldsSetup.put(LANGUAGE, TokenDTO::setLanguage);
        this.optionalFieldsSetup.put(DEFAULT_PROFILE_TYPE, (req, type) -> req.setDefaultProfileType(
                parseEnum(type, DEFAULT_PROFILE_TYPE, ProfileType.class)));
        this.optionalFieldsSetup.put(ENERGY_CONTRACT, (req, energyContract) -> req.setEnergyContract(
                parseModelFromJson(energyContract, ENERGY_CONTRACT, EnergyContract.class)));
    }

    @Override
    public TokenDTO createMessageWithValidatedParams(Map<String, String> params) {
        TokenDTO cdrDTO = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + cdrDTO);
        return cdrDTO;
    }
}
