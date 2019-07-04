package com.hungnn.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class CheckListIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("CheckListIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        final Map<String, Object> persistentAttributes = handlerInput
                .getAttributesManager()
                .getPersistentAttributes();

        final List<String> peopleList = (List<String>) persistentAttributes.get("people");
        if (peopleList == null || peopleList.size() == 0) {
            return handlerInput.getResponseBuilder()
                    .withSpeech("The list is empty. Please add more people to the list.")
                    .withReprompt("Please add more people to the list.")
                    .build();
        }

        final String speechText = "There is " + peopleList.size() + " in the list."
                + peopleList.toString();
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt("What can i help you?")
                .build();
    }
}
