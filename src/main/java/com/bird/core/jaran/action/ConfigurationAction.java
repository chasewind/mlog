package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.ContextUtil;
import com.bird.core.InterpretationContext;
import com.bird.core.OptionHelper;

public class ConfigurationAction extends Action {

    static final String INTERNAL_DEBUG_ATTR       = "debug";
    static final String DEBUG_SYSTEM_PROPERTY_KEY = "logback-access.debug";

    @Override
    public void begin(InterpretationContext ec, String name, Attributes attributes) {

        // See LBCLASSIC-225 (the system property is looked up first. Thus, it overrides
        // the equivalent property in the config file. This reversal of scope priority is justified
        // by the use case: the admin trying to chase rogue config file
        String debugAttrib = System.getProperty(DEBUG_SYSTEM_PROPERTY_KEY);
        if (debugAttrib == null) {
            debugAttrib = attributes.getValue(INTERNAL_DEBUG_ATTR);
        }

        if (OptionHelper.isEmpty(debugAttrib) || debugAttrib.equals("false") || debugAttrib.equals("null")) {
            addInfo(INTERNAL_DEBUG_ATTR + " attribute not set");
        } else {
            // StatusListenerConfigHelper.addOnConsoleListenerInstance(context, new OnConsoleStatusListener());
        }

        new ContextUtil(context).addHostNameAsProperty();

        // the context is appender attachable, so it is pushed on top of the stack
        ec.pushObject(getContext());
    }

    @Override
    public void end(InterpretationContext ec, String name) {
        addInfo("End of configuration.");
        ec.popObject();
    }
}
