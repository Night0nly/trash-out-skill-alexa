package com.hungnn.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class TrashDumpedIntent implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("TrashDumpedIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        final Map<String, Object> persistentAttributes = handlerInput
                .getAttributesManager()
                .getPersistentAttributes();

        final Integer turn = (Integer) persistentAttributes.get("turn");


        final String speechText = "Bravo, next turn is ";
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .build();
    }
}
