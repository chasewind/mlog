package com.bird.core.jaran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bird.core.Appender;
import com.bird.core.ElementSelector;
import com.bird.core.InterpretationContext;
import com.bird.core.Interpreter;
import com.bird.core.jaran.action.AppenderAction;
import com.bird.core.jaran.action.AppenderRefAction;
import com.bird.core.jaran.action.ContextPropertyAction;
import com.bird.core.jaran.action.ConversionRuleAction;
import com.bird.core.jaran.action.DefinePropertyAction;
import com.bird.core.jaran.action.NestedBasicPropertyIA;
import com.bird.core.jaran.action.NestedComplexPropertyIA;
import com.bird.core.jaran.action.NewRuleAction;
import com.bird.core.jaran.action.ParamAction;
import com.bird.core.jaran.action.PropertyAction;
import com.bird.core.jaran.action.ShutdownHookAction;
import com.bird.core.jaran.action.TimestampAction;

abstract public class JoranConfiguratorBase extends GenericConfigurator {

    public List getErrorList() {
        return null;
    }

    @Override
    protected void addInstanceRules(RuleStore rs) {

        // is "configuration/variable" referenced in the docs?
        rs.addRule(new ElementSelector("configuration/variable"), new PropertyAction());
        rs.addRule(new ElementSelector("configuration/property"), new PropertyAction());

        rs.addRule(new ElementSelector("configuration/substitutionProperty"), new PropertyAction());

        rs.addRule(new ElementSelector("configuration/timestamp"), new TimestampAction());
        rs.addRule(new ElementSelector("configuration/shutdownHook"), new ShutdownHookAction());
        rs.addRule(new ElementSelector("configuration/define"), new DefinePropertyAction());

        // the contextProperty pattern is deprecated. It is undocumented
        // and will be dropped in future versions of logback
        rs.addRule(new ElementSelector("configuration/contextProperty"), new ContextPropertyAction());

        rs.addRule(new ElementSelector("configuration/conversionRule"), new ConversionRuleAction());

        rs.addRule(new ElementSelector("configuration/appender"), new AppenderAction());
        rs.addRule(new ElementSelector("configuration/appender/appender-ref"), new AppenderRefAction());
        rs.addRule(new ElementSelector("configuration/newRule"), new NewRuleAction());
        rs.addRule(new ElementSelector("*/param"), new ParamAction(getBeanDescriptionCache()));
    }

    @Override
    protected void addImplicitRules(Interpreter interpreter) {
        // The following line adds the capability to parse nested components
        NestedComplexPropertyIA nestedComplexPropertyIA = new NestedComplexPropertyIA(getBeanDescriptionCache());
        nestedComplexPropertyIA.setContext(context);
        interpreter.addImplicitAction(nestedComplexPropertyIA);

        NestedBasicPropertyIA nestedBasicIA = new NestedBasicPropertyIA(getBeanDescriptionCache());
        nestedBasicIA.setContext(context);
        interpreter.addImplicitAction(nestedBasicIA);
    }

    @Override
    protected void buildInterpreter() {
        super.buildInterpreter();
        Map<String, Object> omap = interpreter.getInterpretationContext().getObjectMap();
        omap.put(ActionConst.APPENDER_BAG, new HashMap<String, Appender<?>>());
        omap.put(ActionConst.FILTER_CHAIN_BAG, new HashMap());
    }

    public InterpretationContext getInterpretationContext() {
        return interpreter.getInterpretationContext();
    }
}
