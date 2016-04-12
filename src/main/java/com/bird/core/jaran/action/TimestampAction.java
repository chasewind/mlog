package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.ActionException;
import com.bird.core.CachingDateFormatter;
import com.bird.core.InterpretationContext;
import com.bird.core.OptionHelper;
import com.bird.core.jaran.action.ActionUtil.Scope;

public class TimestampAction extends Action {

    static String DATE_PATTERN_ATTRIBUTE   = "datePattern";
    static String TIME_REFERENCE_ATTRIBUTE = "timeReference";
    static String CONTEXT_BIRTH            = "contextBirth";

    boolean       inError                  = false;

    @Override
    public void begin(InterpretationContext ec, String name, Attributes attributes) throws ActionException {
        String keyStr = attributes.getValue(KEY_ATTRIBUTE);
        if (OptionHelper.isEmpty(keyStr)) {
            addError("Attribute named [" + KEY_ATTRIBUTE + "] cannot be empty");
            inError = true;
        }
        String datePatternStr = attributes.getValue(DATE_PATTERN_ATTRIBUTE);
        if (OptionHelper.isEmpty(datePatternStr)) {
            addError("Attribute named [" + DATE_PATTERN_ATTRIBUTE + "] cannot be empty");
            inError = true;
        }

        String timeReferenceStr = attributes.getValue(TIME_REFERENCE_ATTRIBUTE);
        long timeReference;
        if (CONTEXT_BIRTH.equalsIgnoreCase(timeReferenceStr)) {
            addInfo("Using context birth as time reference.");
            timeReference = context.getBirthTime();
        } else {
            timeReference = System.currentTimeMillis();
            addInfo("Using current interpretation time, i.e. now, as time reference.");
        }

        if (inError) return;

        String scopeStr = attributes.getValue(SCOPE_ATTRIBUTE);
        Scope scope = ActionUtil.stringToScope(scopeStr);

        CachingDateFormatter sdf = new CachingDateFormatter(datePatternStr);
        String val = sdf.format(timeReference);

        addInfo("Adding property to the context with key=\"" + keyStr + "\" and value=\"" + val + "\" to the " + scope
                + " scope");
        ActionUtil.setProperty(ec, keyStr, val, scope);
    }

    @Override
    public void end(InterpretationContext ec, String name) throws ActionException {
    }

}
