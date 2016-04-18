package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.FinalLog;
import com.bird.core.InterpretationContext;
import com.bird.core.Level;
import com.bird.core.Log;
import com.bird.core.LogContext;
import com.bird.core.OptionHelper;
import com.bird.core.jaran.ActionConst;

public class RootLoggerAction extends Action {

    FinalLog root;
    boolean  inError = false;

    public void begin(InterpretationContext ec, String name, Attributes attributes) {
        inError = false;

        LogContext loggerContext = (LogContext) this.context;
        root = loggerContext.getLog(Log.ROOT_LOGGER_NAME);

        String levelStr = ec.subst(attributes.getValue(ActionConst.LEVEL_ATTRIBUTE));
        if (!OptionHelper.isEmpty(levelStr)) {
            Level level = Level.toLevel(levelStr);
            addInfo("Setting level of ROOT logger to " + level);
            root.setLevel(level);
        }
        ec.pushObject(root);
    }

    public void end(InterpretationContext ec, String name) {
        if (inError) {
            return;
        }
        Object o = ec.peekObject();
        if (o != root) {
            addError("The object on the top the of the stack is not the root logger");
            addError("It is: " + o);
        } else {
            ec.popObject();
        }
    }

    public void finish(InterpretationContext ec) {
    }
}
