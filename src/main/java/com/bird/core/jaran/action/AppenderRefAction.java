package com.bird.core.jaran.action;

import java.util.HashMap;

import org.xml.sax.Attributes;

import com.bird.core.AppenderAttachable;
import com.bird.core.CoreConstants;
import com.bird.core.InterpretationContext;
import com.bird.core.OptionHelper;
import com.bird.core.appender.Appender;
import com.bird.core.jaran.ActionConst;

public class AppenderRefAction<E> extends Action {

    boolean inError = false;

    @SuppressWarnings("unchecked")
    public void begin(InterpretationContext ec, String tagName, Attributes attributes) {
        // Let us forget about previous errors (in this object)
        inError = false;

        // logger.debug("begin called");

        Object o = ec.peekObject();

        if (!(o instanceof AppenderAttachable)) {
            String errMsg = "Could not find an AppenderAttachable at the top of execution stack. Near [" + tagName
                            + "] line " + getLineNumber(ec);
            inError = true;
            addError(errMsg);
            return;
        }

        AppenderAttachable<E> appenderAttachable = (AppenderAttachable<E>) o;

        String appenderName = ec.subst(attributes.getValue(ActionConst.REF_ATTRIBUTE));

        if (OptionHelper.isEmpty(appenderName)) {
            // print a meaningful error message and return
            String errMsg = "Missing appender ref attribute in <appender-ref> tag.";
            inError = true;
            addError(errMsg);

            return;
        }

        HashMap<String, Appender<E>> appenderBag = (HashMap<String, Appender<E>>) ec.getObjectMap().get(ActionConst.APPENDER_BAG);
        Appender<E> appender = (Appender<E>) appenderBag.get(appenderName);

        if (appender == null) {
            String msg = "Could not find an appender named [" + appenderName
                         + "]. Did you define it below instead of above in the configuration file?";
            inError = true;
            addError(msg);
            addError("See " + CoreConstants.CODES_URL + "#appender_order for more details.");
            return;
        }

        addInfo("Attaching appender named [" + appenderName + "] to " + appenderAttachable);
        appenderAttachable.addAppender(appender);
    }

    public void end(InterpretationContext ec, String n) {
    }

}
