package com.extrawest.bdd_cpo_ocpi.validation.request;


import com.extrawest.bdd_cpo_ocpi.validation.OutgoingMessageFieldsFactory;
import com.extrawest.bdd_cpo_ocpi.validation.RequestMessageFactory;
import com.extrawest.ocpi.model.dto.DisplayText;
import com.extrawest.ocpi.model.dto.command.CommandResult;
import com.extrawest.ocpi.model.enums.CommandResultType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandResultBddHandler extends OutgoingMessageFieldsFactory<CommandResult>
        implements RequestMessageFactory<CommandResult> {
    public static final String RESULT_REQUIRED = "result";

    public static final String MESSAGE = "message";

    @PostConstruct
    private void init() {

        this.requiredFieldsSetup = new HashMap<>();
        this.requiredFieldsSetup.put(RESULT_REQUIRED, (req, result) -> req.setResult(
                parseEnumOrRandom(result, RESULT_REQUIRED, CommandResultType.class)));

        this.optionalFieldsSetup = Map.of(MESSAGE, (req, message) -> {
            if (nonEqual(wildCard, message)) {
                DisplayText displayText = parseModelFromJson(message, MESSAGE, DisplayText.class);
                req.setMessage(displayText);
            }
        });
    }

    @Override
    public CommandResult createMessageWithValidatedParams(Map<String, String> params) {
        CommandResult commandResult = super.createMessageWithValidatedParamsViaLibModel(params);
        log.info(getParameterizeClassName() + ": " + commandResult);
        return commandResult;
    }
}
