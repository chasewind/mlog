package com.bird.core;

import org.xml.sax.Locator;

public class CAI_WithLocatorSupport extends ContextAwareImpl {

    CAI_WithLocatorSupport(Context context, Interpreter interpreter){
        super(context, interpreter);
    }

    @Override
    protected Object getOrigin() {
        Interpreter i = (Interpreter) super.getOrigin();
        Locator locator = i.locator;
        if (locator != null) {
            return Interpreter.class.getName() + "@" + locator.getLineNumber() + ":" + locator.getColumnNumber();
        } else {
            return Interpreter.class.getName() + "@NA:NA";
        }
    }

    public void addError(String errMsg) {
        System.err.println(errMsg);

    }
}
