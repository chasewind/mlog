package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.ElementSelector;
import com.bird.core.InterpretationContext;
import com.bird.core.OptionHelper;

public class NewRuleAction extends Action {

    boolean inError = false;

    /**
     * Instantiates an layout of the given class and sets its name.
     */
    public void begin(InterpretationContext ec, String localName, Attributes attributes) {
        // Let us forget about previous errors (in this object)
        inError = false;
        String errorMsg;
        String pattern = attributes.getValue(Action.PATTERN_ATTRIBUTE);
        String actionClass = attributes.getValue(Action.ACTION_CLASS_ATTRIBUTE);

        if (OptionHelper.isEmpty(pattern)) {
            inError = true;
            errorMsg = "No 'pattern' attribute in <newRule>";
            addError(errorMsg);
            return;
        }

        if (OptionHelper.isEmpty(actionClass)) {
            inError = true;
            errorMsg = "No 'actionClass' attribute in <newRule>";
            addError(errorMsg);
            return;
        }

        try {
            addInfo("About to add new Joran parsing rule [" + pattern + "," + actionClass + "].");
            ec.getJoranInterpreter().getRuleStore().addRule(new ElementSelector(pattern), actionClass);
        } catch (Exception oops) {
            inError = true;
            errorMsg = "Could not add new Joran parsing rule [" + pattern + "," + actionClass + "]";
            addError(errorMsg);
        }
    }

    /**
     * Once the children elements are also parsed, now is the time to activate the appender options.
     */
    public void end(InterpretationContext ec, String n) {
    }

    public void finish(InterpretationContext ec) {
    }
}
