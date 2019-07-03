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

public class TrashTurnInitIntent implements RequestHandler {
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

        final Slot nameSlot = slots.get(NAME_SLOT);

        if (nameSlot == null) {
            return handlerInput
                    .getResponseBuilder()
                    .withSpeech("Who do you want to add")
                    .build();
        }

        final String name = nameSlot.getValue();

        if (name.isEmpty()) {
            return handlerInput
                    .getResponseBuilder()
                    .withSpeech("Who do you want to add cua name empty test")
                    .build();
        }

        final Map<String, Object> persistentAttributes = handlerInput
                .getAttributesManager()
                .getPersistentAttributes();

        if (!persistentAttributes.containsKey("people")) {
            persistentAttributes.put("people", Arrays.asList(name));
        } else {
            List<String> peopleList = (List<String>) persistentAttributes.get("people");
            peopleList.add(name);
            persistentAttributes.put("people", peopleList);
        }

        handlerInput.getAttributesManager().setPersistentAttributes(persistentAttributes);
        handlerInput.getAttributesManager().savePersistentAttributes();
        return handlerInput
                .getResponseBuilder()
                .withSpeech("Added " + name + "to dump trash turn")
                .build();
    }
}
