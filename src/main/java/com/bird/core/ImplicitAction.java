package com.bird.core;

import org.xml.sax.Attributes;

import com.bird.core.jaran.action.Action;

public abstract class ImplicitAction extends Action {

    public abstract boolean isApplicable(ElementPath currentElementPath, Attributes attributes,
                                         InterpretationContext ec);

}
