package com.hungnn.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class TrashDumpedIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("TrashDumpedIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        final Map<String, Object> persistentAttributes = handlerInput
                .getAttributesManager()
                .getPersistentAttributes();

        final List<String> peopleList = (List<String>) persistentAttributes.get("people");
        if (peopleList == null || peopleList.size() == 0) {
            return handlerInput.getResponseBuilder()
                    .withSpeech("Thank you for dumping the trash. There is no one in the database. Please add more people.")
                    .withReprompt("Please add people to make turn.")
                    .build();
        }

        final BigDecimal turn = (BigDecimal) persistentAttributes.get("turn");
        if (turn == null || turn.intValue() + 1 >= peopleList.size()) {
            persistentAttributes.put("turn", 0);
            handlerInput.getAttributesManager().setPersistentAttributes(persistentAttributes);
            handlerInput.getAttributesManager().savePersistentAttributes();
        } else {
            persistentAttributes.put("turn", turn.intValue() + 1);
            handlerInput.getAttributesManager().setPersistentAttributes(persistentAttributes);
            handlerInput.getAttributesManager().savePersistentAttributes();
        }

        final String speechText = "Bravo, next is "+
                peopleList.get((int) persistentAttributes.get("turn"));
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt("Do you want anything else.")
                .build();
    }
}
