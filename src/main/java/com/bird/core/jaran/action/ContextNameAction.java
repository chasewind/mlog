package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.InterpretationContext;

public class ContextNameAction extends Action {

    public void begin(InterpretationContext ec, String name, Attributes attributes) {
    }

    public void body(InterpretationContext ec, String body) {

        String finalBody = ec.subst(body);
        addInfo("Setting logger context name as [" + finalBody + "]");
        try {
            context.setName(finalBody);
        } catch (IllegalStateException e) {
            addError("Failed to rename context [" + context.getName() + "] as [" + finalBody + "]", e);
        }
    }

    public void end(InterpretationContext ec, String name) {
    }
}
