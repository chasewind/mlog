package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.FinalLog;
import com.bird.core.InterpretationContext;
import com.bird.core.Level;
import com.bird.core.jaran.ActionConst;

public class LevelAction extends Action {

    boolean inError = false;

    public void begin(InterpretationContext ec, String name, Attributes attributes) {
        Object o = ec.peekObject();

        if (!(o instanceof FinalLog)) {
            inError = true;
            addError("For element <level>, could not find a logger at the top of execution stack.");
            return;
        }

        FinalLog l = (FinalLog) o;

        String loggerName = l.getName();

        String levelStr = ec.subst(attributes.getValue(ActionConst.VALUE_ATTR));
        // addInfo("Encapsulating logger name is [" + loggerName
        // + "], level value is [" + levelStr + "].");

        if (ActionConst.INHERITED.equalsIgnoreCase(levelStr) || ActionConst.NULL.equalsIgnoreCase(levelStr)) {
            l.setLevel(null);
        } else {
            l.setLevel(Level.toLevel(levelStr, Level.DEBUG));
        }

        addInfo(loggerName + " level set to " + l.getLevel());
    }

    public void finish(InterpretationContext ec) {
    }

    public void end(InterpretationContext ec, String e) {
    }
}
