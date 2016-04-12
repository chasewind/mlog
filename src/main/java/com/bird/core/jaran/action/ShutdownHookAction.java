package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.ActionException;
import com.bird.core.CoreConstants;
import com.bird.core.InterpretationContext;
import com.bird.core.OptionHelper;
import com.bird.core.hook.ShutdownHookBase;

public class ShutdownHookAction extends Action {

    ShutdownHookBase hook;
    private boolean  inError;

    /**
     * Instantiates a shutdown hook of the given class and sets its name. The hook thus generated is placed in the
     * {@link InterpretationContext}'s shutdown hook bag.
     */
    @Override
    public void begin(InterpretationContext ic, String name, Attributes attributes) throws ActionException {
        hook = null;
        inError = false;

        String className = attributes.getValue(CLASS_ATTRIBUTE);
        if (OptionHelper.isEmpty(className)) {
            addError("Missing class name for shutdown hook. Near [" + name + "] line " + getLineNumber(ic));
            inError = true;
            return;
        }

        try {
            addInfo("About to instantiate shutdown hook of type [" + className + "]");

            hook = (ShutdownHookBase) OptionHelper.instantiateByClassName(className, ShutdownHookBase.class, context);
            hook.setContext(context);

            ic.pushObject(hook);
        } catch (Exception e) {
            inError = true;
            addError("Could not create a shutdown hook of type [" + className + "].", e);
            throw new ActionException(e);
        }
    }

    /**
     * Once the children elements are also parsed, now is the time to activate the shutdown hook options.
     */
    @Override
    public void end(InterpretationContext ic, String name) throws ActionException {
        if (inError) {
            return;
        }

        Object o = ic.peekObject();
        if (o != hook) {
            addError("The object at the of the stack is not the hook pushed earlier.");
        } else {
            ic.popObject();

            Thread hookThread = new Thread(hook, "Logback shutdown hook [" + context.getName() + "]");

            context.putObject(CoreConstants.SHUTDOWN_HOOK_THREAD, hookThread);
            Runtime.getRuntime().addShutdownHook(hookThread);
        }
    }
}
