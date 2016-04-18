package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.ActionException;
import com.bird.core.ContextAware;
import com.bird.core.InterpretationContext;
import com.bird.core.LifeCycle;
import com.bird.core.LogContext;
import com.bird.core.LoggerContextListener;
import com.bird.core.OptionHelper;

public class LoggerContextListenerAction extends Action {

    boolean               inError = false;
    LoggerContextListener lcl;

    @Override
    public void begin(InterpretationContext ec, String name, Attributes attributes) throws ActionException {

        inError = false;

        String className = attributes.getValue(CLASS_ATTRIBUTE);
        if (OptionHelper.isEmpty(className)) {
            addError("Mandatory \"" + CLASS_ATTRIBUTE + "\" attribute not set for <loggerContextListener> element");
            inError = true;
            return;
        }

        try {
            lcl = (LoggerContextListener) OptionHelper.instantiateByClassName(className, LoggerContextListener.class,
                                                                              context);

            if (lcl instanceof ContextAware) {
                ((ContextAware) lcl).setContext(context);
            }

            ec.pushObject(lcl);
            addInfo("Adding LoggerContextListener of type [" + className + "] to the object stack");

        } catch (Exception oops) {
            inError = true;
            addError("Could not create LoggerContextListener of type " + className + "].", oops);
        }
    }

    @Override
    public void end(InterpretationContext ec, String name) throws ActionException {
        if (inError) {
            return;
        }
        Object o = ec.peekObject();

        if (o != lcl) {
            addError("The object on the top the of the stack is not the LoggerContextListener pushed earlier.");
        } else {
            if (lcl instanceof LifeCycle) {
                ((LifeCycle) lcl).start();
                addInfo("Starting LoggerContextListener");
            }
            ((LogContext) context).addListener(lcl);
            ec.popObject();
        }
    }

}
