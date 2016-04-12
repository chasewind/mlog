package com.bird.core.jaran.action;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.bird.core.ActionException;
import com.bird.core.ContextAwareBase;
import com.bird.core.InterpretationContext;
import com.bird.core.Interpreter;

public abstract class Action extends ContextAwareBase {

    public static final String NAME_ATTRIBUTE         = "name";
    public static final String KEY_ATTRIBUTE          = "key";
    public static final String VALUE_ATTRIBUTE        = "value";
    public static final String FILE_ATTRIBUTE         = "file";
    public static final String CLASS_ATTRIBUTE        = "class";
    public static final String PATTERN_ATTRIBUTE      = "pattern";
    public static final String SCOPE_ATTRIBUTE        = "scope";

    public static final String ACTION_CLASS_ATTRIBUTE = "actionClass";

    /**
     * Called when the parser encounters an element matching a {@link ch.qos.logback.core.joran.spi.ElementSelector
     * Pattern}.
     */
    public abstract void begin(InterpretationContext ic, String name, Attributes attributes) throws ActionException;

    /**
     * Called to pass the body (as text) contained within an element.
     * 
     * @param ic
     * @param body
     * @throws ActionException
     */
    public void body(InterpretationContext ic, String body) throws ActionException {
        // NOP
    }

    /*
     * Called when the parser encounters an endElement event matching a {@link ch.qos.logback.core.joran.spi.Pattern
     * Pattern}.
     */
    public abstract void end(InterpretationContext ic, String name) throws ActionException;

    public String toString() {
        return this.getClass().getName();
    }

    protected int getColumnNumber(InterpretationContext ic) {
        Interpreter ji = ic.getJoranInterpreter();
        Locator locator = ji.getLocator();
        if (locator != null) {
            return locator.getColumnNumber();
        }
        return -1;
    }

    protected int getLineNumber(InterpretationContext ic) {
        Interpreter ji = ic.getJoranInterpreter();
        Locator locator = ji.getLocator();
        if (locator != null) {
            return locator.getLineNumber();
        }
        return -1;
    }

    protected String getLineColStr(InterpretationContext ic) {
        return "line: " + getLineNumber(ic) + ", column: " + getColumnNumber(ic);
    }
}
