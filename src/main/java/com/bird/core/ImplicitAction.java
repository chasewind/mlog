package com.bird.core;

import org.xml.sax.Attributes;

public abstract class ImplicitAction extends Action {

    public abstract boolean isApplicable(ElementPath currentElementPath, Attributes attributes,
                                         InterpretationContext ec);

}
