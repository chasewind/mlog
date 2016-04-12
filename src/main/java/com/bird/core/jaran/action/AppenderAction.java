package com.bird.core.jaran.action;

import java.util.HashMap;

import org.xml.sax.Attributes;

import com.bird.core.ActionException;
import com.bird.core.Appender;
import com.bird.core.InterpretationContext;
import com.bird.core.LifeCycle;
import com.bird.core.OptionHelper;
import com.bird.core.jaran.ActionConst;

public class AppenderAction<E> extends Action {

    Appender<E>     appender;
    private boolean inError = false;

    /**
     * Instantiates an appender of the given class and sets its name. The appender thus generated is placed in the
     * {@link InterpretationContext}'s appender bag.
     */
    @SuppressWarnings("unchecked")
    public void begin(InterpretationContext ec, String localName, Attributes attributes) throws ActionException {
        // We are just beginning, reset variables
        appender = null;
        inError = false;

        String className = attributes.getValue(CLASS_ATTRIBUTE);
        if (OptionHelper.isEmpty(className)) {
            addError("Missing class name for appender. Near [" + localName + "] line " + getLineNumber(ec));
            inError = true;
            return;
        }

        try {
            addInfo("About to instantiate appender of type [" + className + "]");

            appender = (Appender<E>) OptionHelper.instantiateByClassName(className, Appender.class, context);

            appender.setContext(context);

            String appenderName = ec.subst(attributes.getValue(NAME_ATTRIBUTE));

            if (OptionHelper.isEmpty(appenderName)) {
                addInfo("No appender name given for appender of type " + className + "].");
            } else {
                appender.setName(appenderName);
                addInfo("Naming appender as [" + appenderName + "]");
            }

            // The execution context contains a bag which contains the appenders
            // created thus far.
            HashMap<String, Appender<E>> appenderBag = (HashMap<String, Appender<E>>) ec.getObjectMap().get(ActionConst.APPENDER_BAG);

            // add the appender just created to the appender bag.
            appenderBag.put(appenderName, appender);

            ec.pushObject(appender);
        } catch (Exception oops) {
            inError = true;
            addError("Could not create an Appender of type [" + className + "].", oops);
            throw new ActionException(oops);
        }
    }

    /**
     * Once the children elements are also parsed, now is the time to activate the appender options.
     */
    public void end(InterpretationContext ec, String name) {
        if (inError) {
            return;
        }

        if (appender instanceof LifeCycle) {
            ((LifeCycle) appender).start();
        }

        Object o = ec.peekObject();

        if (o != appender) {
            addInfo("The object at the of the stack is not the appender named [" + appender.getName()
                    + "] pushed earlier.");
        } else {
            ec.popObject();
        }
    }
}
