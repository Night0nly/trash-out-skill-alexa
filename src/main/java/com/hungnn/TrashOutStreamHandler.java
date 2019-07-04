package com.hungnn;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.hungnn.handlers.*;

public class TrashOutStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new LaunchRequestHandler(),
                        new HelpIntentHandler(),
                        new SessionEndedRequestHandler(),
                        new CancelAndStopIntentHandler(),
                        new TrashTurnInitIntentHandler(),
                        new TrashDumpedIntentHandler(),
                        new RemovePeopleIntentHandler(),
                        new CheckListIntentHandler()
                )
                .withTableName("trash-turn")
                .withAutoCreateTable(true)
                .build();
    }

    public TrashOutStreamHandler() {
        super(getSkill());
    }
}
