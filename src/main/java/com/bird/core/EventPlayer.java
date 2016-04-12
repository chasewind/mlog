package com.bird.core;

import java.util.ArrayList;
import java.util.List;

public class EventPlayer {

    final Interpreter interpreter;
    List<SaxEvent>    eventList;
    int               currentIndex;

    public EventPlayer(Interpreter interpreter){
        this.interpreter = interpreter;
    }

    /**
     * Return a copy of the current event list in the player.
     * 
     * @return
     * @since 0.9.20
     */
    public List<SaxEvent> getCopyOfPlayerEventList() {
        return new ArrayList<SaxEvent>(eventList);
    }

    public void play(List<SaxEvent> aSaxEventList) {
        eventList = aSaxEventList;
        SaxEvent se;
        for (currentIndex = 0; currentIndex < eventList.size(); currentIndex++) {
            se = eventList.get(currentIndex);

            if (se instanceof StartEvent) {
                interpreter.startElement((StartEvent) se);
                // invoke fireInPlay after startElement processing
                interpreter.getInterpretationContext().fireInPlay(se);
            }
            if (se instanceof BodyEvent) {
                // invoke fireInPlay before characters processing
                interpreter.getInterpretationContext().fireInPlay(se);
                interpreter.characters((BodyEvent) se);
            }
            if (se instanceof EndEvent) {
                // invoke fireInPlay before endElement processing
                interpreter.getInterpretationContext().fireInPlay(se);
                interpreter.endElement((EndEvent) se);
            }

        }
    }

    public void addEventsDynamically(List<SaxEvent> eventList, int offset) {
        this.eventList.addAll(currentIndex + offset, eventList);
    }
}
