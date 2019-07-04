package com.hungnn.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(requestType(LaunchRequest.class).or(intentName("WhosNextIntent")));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        final Map<String, Object> persistentAttributes = handlerInput
                .getAttributesManager()
                .getPersistentAttributes();

        final List<String> peopleList = (List<String>) persistentAttributes.get("people");

        System.out.print("People List : ");
        System.out.println(peopleList);

        if (peopleList == null || peopleList.size() == 0) {
            return handlerInput.getResponseBuilder()
                    .withSpeech("Hello this is trash manager. Please add people to make turn")
                    .withReprompt("Please add people to make turn")
                    .build();
        }

        final BigDecimal turn = (BigDecimal) persistentAttributes.get("turn");
        System.out.print("Turn : ");
        System.out.println(turn);

        if (turn == null || turn.intValue() >= peopleList.size()) {
            persistentAttributes.put("turn", 0);
            handlerInput.getAttributesManager().setPersistentAttributes(persistentAttributes);
            handlerInput.getAttributesManager().savePersistentAttributes();
        }

        final String speechText = "Hello this is trash manager." +
                " Has the trash been dumped yet." +
                " This turn is " + peopleList.get(((BigDecimal) persistentAttributes.get("turn")).intValue()) + "'s turn.";
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt(speechText)
                .build();
    }
}
