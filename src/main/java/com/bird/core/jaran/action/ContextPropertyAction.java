package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.ActionException;
import com.bird.core.InterpretationContext;

public class ContextPropertyAction extends Action {

    @Override
    public void begin(InterpretationContext ec, String name, Attributes attributes) throws ActionException {
        addError("The [contextProperty] element has been removed. Please use [substitutionProperty] element instead");
    }

    @Override
    public void end(InterpretationContext ec, String name) throws ActionException {
    }

}
