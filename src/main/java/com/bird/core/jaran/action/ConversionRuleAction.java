package com.bird.core.jaran.action;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

import com.bird.core.CoreConstants;
import com.bird.core.InterpretationContext;
import com.bird.core.OptionHelper;
import com.bird.core.jaran.ActionConst;

public class ConversionRuleAction extends Action {

    boolean inError = false;

    /**
     * Instantiates an layout of the given class and sets its name.
     */
    @SuppressWarnings("unchecked")
    public void begin(InterpretationContext ec, String localName, Attributes attributes) {
        // Let us forget about previous errors (in this object)
        inError = false;

        String errorMsg;
        String conversionWord = attributes.getValue(ActionConst.CONVERSION_WORD_ATTRIBUTE);
        String converterClass = attributes.getValue(ActionConst.CONVERTER_CLASS_ATTRIBUTE);

        if (OptionHelper.isEmpty(conversionWord)) {
            inError = true;
            errorMsg = "No 'conversionWord' attribute in <conversionRule>";
            addError(errorMsg);

            return;
        }

        if (OptionHelper.isEmpty(converterClass)) {
            inError = true;
            errorMsg = "No 'converterClass' attribute in <conversionRule>";
            ec.addError(errorMsg);

            return;
        }

        try {
            Map<String, String> ruleRegistry = (Map) context.getObject(CoreConstants.PATTERN_RULE_REGISTRY);
            if (ruleRegistry == null) {
                ruleRegistry = new HashMap<String, String>();
                context.putObject(CoreConstants.PATTERN_RULE_REGISTRY, ruleRegistry);
            }
            // put the new rule into the rule registry
            addInfo("registering conversion word " + conversionWord + " with class [" + converterClass + "]");
            ruleRegistry.put(conversionWord, converterClass);
        } catch (Exception oops) {
            inError = true;
            errorMsg = "Could not add conversion rule to PatternLayout.";
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
