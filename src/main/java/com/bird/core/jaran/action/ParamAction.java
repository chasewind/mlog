package com.bird.core.jaran.action;

import org.xml.sax.Attributes;

import com.bird.core.InterpretationContext;
import com.bird.core.jaran.BeanDescriptionCache;
import com.bird.core.jaran.PropertySetter;

public class ParamAction extends Action {

    static String                      NO_NAME  = "No name attribute in <param> element";
    static String                      NO_VALUE = "No name attribute in <param> element";
    boolean                            inError  = false;

    private final BeanDescriptionCache beanDescriptionCache;

    public ParamAction(BeanDescriptionCache beanDescriptionCache){
        this.beanDescriptionCache = beanDescriptionCache;
    }

    public void begin(InterpretationContext ec, String localName, Attributes attributes) {
        String name = attributes.getValue(NAME_ATTRIBUTE);
        String value = attributes.getValue(VALUE_ATTRIBUTE);

        if (name == null) {
            inError = true;
            addError(NO_NAME);
            return;
        }

        if (value == null) {
            inError = true;
            addError(NO_VALUE);
            return;
        }

        // remove both leading and trailing spaces
        value = value.trim();

        Object o = ec.peekObject();
        PropertySetter propSetter = new PropertySetter(beanDescriptionCache, o);
        propSetter.setContext(context);
        value = ec.subst(value);

        // allow for variable substitution for name as well
        name = ec.subst(name);

        // getLogger().debug(
        // "In ParamAction setting parameter [{}] to value [{}].", name, value);
        propSetter.setProperty(name, value);
    }

    public void end(InterpretationContext ec, String localName) {
    }

    public void finish(InterpretationContext ec) {
    }
}
