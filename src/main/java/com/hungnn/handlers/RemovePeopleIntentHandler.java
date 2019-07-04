package com.hungnn.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class RemovePeopleIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("RemovePeopleIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        final Map<String, Object> persistentAttributes = handlerInput
                .getAttributesManager()
                .getPersistentAttributes();

        final List<String> peopleList = (List<String>) persistentAttributes.get("people");
        if (peopleList == null || peopleList.size() == 0) {
            return handlerInput.getResponseBuilder()
                    .withSpeech("You can't delete people because there is no people in the database." +
                            " Please add more people")
                    .withReprompt("Please add people to make turn")
                    .build();
        }

        final IntentRequest request = (IntentRequest) handlerInput.getRequestEnvelope().getRequest();
        final Intent intent = request.getIntent();
        final Map<String, Slot> slots = intent.getSlots();

        final String name = slots.get("Name").getValue();

        if (!peopleList.contains(name)) {
            return handlerInput.getResponseBuilder()
                    .withSpeech("There is no " + name + " in the list.")
                    .withReprompt("Do you want anything else.")
                    .build();
        }

        peopleList.remove(name);
        persistentAttributes.put("people", peopleList);
        handlerInput.getAttributesManager().setPersistentAttributes(persistentAttributes);
        handlerInput.getAttributesManager().savePersistentAttributes();

        if (peopleList.size() == 0) {
            return handlerInput.getResponseBuilder()
                    .withSpeech(name + " is removed from the list." +
                            "The list is empty now. Please add more people to list.")
                    .withReprompt("Please add more people to list.")
                    .build();
        }

        return handlerInput.getResponseBuilder()
                .withSpeech(name + " is removed from the list.")
                .build();
    }
}
