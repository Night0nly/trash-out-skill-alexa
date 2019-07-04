package com.hungnn.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.lang.reflect.Array;
import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

public class TrashTurnInitIntentHandler implements RequestHandler {
    public static final String NAME_SLOT = "Name";

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("TrashTurnInitIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        final IntentRequest request = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();
        final Intent intent = request.getIntent();
        final Map<String, Slot> slots = intent.getSlots();
        final String name = slots.get(NAME_SLOT).getValue();

        final Map<String, Object> persistentAttributes = handlerInput
                .getAttributesManager()
                .getPersistentAttributes();

        if (!persistentAttributes.containsKey("people")) {
            persistentAttributes.put("people", Arrays.asList(name));
        } else {
            List<String> peopleList = (List<String>) persistentAttributes.get("people");
            if (peopleList.contains(name)) {
                final String speechText = "This person has already been added to the list.";
                return handlerInput.getResponseBuilder()
                        .withSpeech(speechText)
                        .withReprompt(speechText)
                        .build();
            }
            peopleList.add(name);
            persistentAttributes.put("people", peopleList);
        }

        handlerInput.getAttributesManager().setPersistentAttributes(persistentAttributes);
        handlerInput.getAttributesManager().savePersistentAttributes();
        return handlerInput
                .getResponseBuilder()
                .withSpeech("Added " + name + "to dump trash turn")
                .withReprompt("Do you want anything else.")
                .build();
    }
}
