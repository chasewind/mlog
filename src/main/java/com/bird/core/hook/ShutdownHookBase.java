package com.bird.core.hook;

import com.bird.core.Context;
import com.bird.core.ContextAwareBase;
import com.bird.core.ContextBase;

public abstract class ShutdownHookBase extends ContextAwareBase implements ShutdownHook {

    public ShutdownHookBase(){
    }

    /**
     * Default method for stopping the Logback context
     */
    protected void stop() {
        addInfo("Logback context being closed via shutdown hook");

        Context hookContext = getContext();
        if (hookContext instanceof ContextBase) {
            ContextBase context = (ContextBase) hookContext;
            context.stop();
        }
    }
}
