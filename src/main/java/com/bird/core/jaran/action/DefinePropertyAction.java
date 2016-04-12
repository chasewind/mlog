package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.ActionException;
import com.bird.core.InterpretationContext;
import com.bird.core.LifeCycle;
import com.bird.core.OptionHelper;
import com.bird.core.PropertyDefiner;
import com.bird.core.jaran.action.ActionUtil.Scope;

public class DefinePropertyAction extends Action {

    String          scopeStr;
    Scope           scope;
    String          propertyName;
    PropertyDefiner definer;
    boolean         inError;

    public void begin(InterpretationContext ec, String localName, Attributes attributes) throws ActionException {
        // reset variables
        scopeStr = null;
        scope = null;
        propertyName = null;
        definer = null;
        inError = false;

        // read future property name
        propertyName = attributes.getValue(NAME_ATTRIBUTE);
        scopeStr = attributes.getValue(SCOPE_ATTRIBUTE);

        scope = ActionUtil.stringToScope(scopeStr);
        if (OptionHelper.isEmpty(propertyName)) {
            addError("Missing property name for property definer. Near [" + localName + "] line " + getLineNumber(ec));
            inError = true;
            return;
        }

        // read property definer class name
        String className = attributes.getValue(CLASS_ATTRIBUTE);
        if (OptionHelper.isEmpty(className)) {
            addError("Missing class name for property definer. Near [" + localName + "] line " + getLineNumber(ec));
            inError = true;
            return;
        }

        // try to instantiate property definer
        try {
            addInfo("About to instantiate property definer of type [" + className + "]");
            definer = (PropertyDefiner) OptionHelper.instantiateByClassName(className, PropertyDefiner.class, context);
            definer.setContext(context);
            if (definer instanceof LifeCycle) {
                ((LifeCycle) definer).start();
            }
            ec.pushObject(definer);
        } catch (Exception oops) {
            inError = true;
            addError("Could not create an PropertyDefiner of type [" + className + "].", oops);
            throw new ActionException(oops);
        }
    }

    /**
     * Now property definer is initialized by all properties and we can put property value to context
     */
    public void end(InterpretationContext ec, String name) {
        if (inError) {
            return;
        }

        Object o = ec.peekObject();

        if (o != definer) {
            addError("The object at the of the stack is not the property definer for property named [" + propertyName
                     + "] pushed earlier.");
        } else {
            addInfo("Popping property definer for property named [" + propertyName + "] from the object stack");
            ec.popObject();
            // let's put defined property and value to context but only if it is
            // not null
            String propertyValue = definer.getPropertyValue();
            if (propertyValue != null) {
                ActionUtil.setProperty(ec, propertyName, propertyValue, scope);
            }
        }
    }
}
