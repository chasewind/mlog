package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.FinalLog;
import com.bird.core.InterpretationContext;
import com.bird.core.Level;
import com.bird.core.LogContext;
import com.bird.core.OptionHelper;
import com.bird.core.jaran.ActionConst;

public class LoggerAction extends Action {

    public static final String LEVEL_ATTRIBUTE = "level";

    boolean                    inError         = false;
    FinalLog                   logger;

    public void begin(InterpretationContext ec, String name, Attributes attributes) {
        // Let us forget about previous errors (in this object)
        inError = false;
        logger = null;

        LogContext loggerContext = (LogContext) this.context;

        String loggerName = ec.subst(attributes.getValue(NAME_ATTRIBUTE));

        if (OptionHelper.isEmpty(loggerName)) {
            inError = true;
            String aroundLine = getLineColStr(ec);
            String errorMsg = "No 'name' attribute in element " + name + ", around " + aroundLine;
            addError(errorMsg);
            return;
        }

        logger = loggerContext.getLog(loggerName);

        String levelStr = ec.subst(attributes.getValue(LEVEL_ATTRIBUTE));

        if (!OptionHelper.isEmpty(levelStr)) {
            if (ActionConst.INHERITED.equalsIgnoreCase(levelStr) || ActionConst.NULL.equalsIgnoreCase(levelStr)) {
                addInfo("Setting level of logger [" + loggerName + "] to null, i.e. INHERITED");
                logger.setLevel(null);
            } else {
                Level level = Level.toLevel(levelStr);
                addInfo("Setting level of logger [" + loggerName + "] to " + level);
                logger.setLevel(level);
            }
        }

        String additivityStr = ec.subst(attributes.getValue(ActionConst.ADDITIVITY_ATTRIBUTE));
        if (!OptionHelper.isEmpty(additivityStr)) {
            boolean additive = OptionHelper.toBoolean(additivityStr, true);
            addInfo("Setting additivity of logger [" + loggerName + "] to " + additive);
            logger.setAdditive(additive);
        }
        ec.pushObject(logger);
    }

    public void end(InterpretationContext ec, String e) {
        if (inError) {
            return;
        }
        Object o = ec.peekObject();
        if (o != logger) {
            addError("The object on the top the of the stack is not " + logger + " pushed earlier");
            addError("It is: " + o);
        } else {
            ec.popObject();
        }
    }

    public void finish(InterpretationContext ec) {
    }
}
