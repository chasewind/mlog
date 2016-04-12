package com.bird.core.jaran;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.bird.core.ElementPath;
import com.bird.core.ImplicitAction;
import com.bird.core.InterpretationContext;

public class NestedBasicPropertyIA extends ImplicitAction {

    // We use a stack of IADataForBasicProperty objects in order to
    // support nested elements which are handled by the same NestedBasicPropertyIA instance.
    // We push a IADataForBasicProperty instance in the isApplicable method (if the
    // action is applicable) and pop it in the end() method.
    // The XML well-formedness property will guarantee that a push will eventually
    // be followed by the corresponding pop.
    Stack<IADataForBasicProperty>      actionDataStack = new Stack<IADataForBasicProperty>();

    private final BeanDescriptionCache beanDescriptionCache;

    public NestedBasicPropertyIA(BeanDescriptionCache beanDescriptionCache){
        this.beanDescriptionCache = beanDescriptionCache;
    }

    public boolean isApplicable(ElementPath elementPath, Attributes attributes, InterpretationContext ec) {
        // System.out.println("in NestedSimplePropertyIA.isApplicable [" + pattern +
        // "]");
        String nestedElementTagName = elementPath.peekLast();

        // no point in attempting if there is no parent object
        if (ec.isEmpty()) {
            return false;
        }

        Object o = ec.peekObject();
        PropertySetter parentBean = new PropertySetter(beanDescriptionCache, o);
        parentBean.setContext(context);

        AggregationType aggregationType = parentBean.computeAggregationType(nestedElementTagName);

        switch (aggregationType) {
            case NOT_FOUND:
            case AS_COMPLEX_PROPERTY:
            case AS_COMPLEX_PROPERTY_COLLECTION:
                return false;

            case AS_BASIC_PROPERTY:
            case AS_BASIC_PROPERTY_COLLECTION:
                IADataForBasicProperty ad = new IADataForBasicProperty(parentBean, aggregationType,
                                                                       nestedElementTagName);
                actionDataStack.push(ad);
                // addInfo("NestedSimplePropertyIA deemed applicable [" + pattern + "]");
                return true;
            default:
                addError("PropertySetter.canContainComponent returned " + aggregationType);
                return false;
        }
    }

    public void begin(InterpretationContext ec, String localName, Attributes attributes) {
        // NOP
    }

    public void body(InterpretationContext ec, String body) {

        String finalBody = ec.subst(body);
        // get the action data object pushed in isApplicable() method call
        IADataForBasicProperty actionData = (IADataForBasicProperty) actionDataStack.peek();
        switch (actionData.aggregationType) {
            case AS_BASIC_PROPERTY:
                actionData.parentBean.setProperty(actionData.propertyName, finalBody);
                break;
            case AS_BASIC_PROPERTY_COLLECTION:
                actionData.parentBean.addBasicProperty(actionData.propertyName, finalBody);
            default:
                addError("Unexpected aggregationType " + actionData.aggregationType);
        }
    }

    public void end(InterpretationContext ec, String tagName) {
        // pop the action data object pushed in isApplicable() method call
        actionDataStack.pop();
    }
}
