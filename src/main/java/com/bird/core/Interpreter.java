package com.bird.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class Interpreter {

    private static List<Action>             EMPTY_LIST = new Vector<Action>(0);

    final private RuleStore                 ruleStore;
    final private InterpretationContext     interpretationContext;
    final private ArrayList<ImplicitAction> implicitActions;
    final private CAI_WithLocatorSupport    cai;
    private ElementPath                     elementPath;
    Locator                                 locator;
    EventPlayer                             eventPlayer;

    /**
     * The <id>actionListStack</id> contains a list of actions that are executing for the given XML element. A list of
     * actions is pushed by the {link #startElement} and popped by {@link #endElement}.
     */
    Stack<List<Action>>                     actionListStack;

    /**
     * If the skip nested is set, then we skip all its nested elements until it is set back to null at when the
     * element's end is reached.
     */
    ElementPath                             skip       = null;

    public Interpreter(Context context, RuleStore rs, ElementPath initialElementPath){
        this.cai = new CAI_WithLocatorSupport(context, this);
        ruleStore = rs;
        interpretationContext = new InterpretationContext(context, this);
        implicitActions = new ArrayList<ImplicitAction>(3);
        this.elementPath = initialElementPath;
        actionListStack = new Stack<List<Action>>();
        eventPlayer = new EventPlayer(this);
    }

    public EventPlayer getEventPlayer() {
        return eventPlayer;
    }

    public void setInterpretationContextPropertiesMap(Map<String, String> propertiesMap) {
        interpretationContext.setPropertiesMap(propertiesMap);
    }

    /**
     * @deprecated replaced by {@link #getInterpretationContext()}
     */
    public InterpretationContext getExecutionContext() {
        return getInterpretationContext();
    }

    public InterpretationContext getInterpretationContext() {
        return interpretationContext;
    }

    public void startDocument() {
    }

    public void startElement(StartEvent se) {
        setDocumentLocator(se.getLocator());
        startElement(se.namespaceURI, se.localName, se.qName, se.attributes);
    }

    private void startElement(String namespaceURI, String localName, String qName, Attributes atts) {

        String tagName = getTagName(localName, qName);
        elementPath.push(tagName);

        if (skip != null) {
            // every startElement pushes an action list
            pushEmptyActionList();
            return;
        }

        List<Action> applicableActionList = getApplicableActionList(elementPath, atts);
        if (applicableActionList != null) {
            actionListStack.add(applicableActionList);
            callBeginAction(applicableActionList, tagName, atts);
        } else {
            // every startElement pushes an action list
            pushEmptyActionList();
            String errMsg = "no applicable action for [" + tagName + "], current ElementPath  is [" + elementPath + "]";
            cai.addError(errMsg);
        }
    }

    /**
     * This method is used to
     */
    private void pushEmptyActionList() {
        actionListStack.add(EMPTY_LIST);
    }

    public void characters(BodyEvent be) {

        setDocumentLocator(be.locator);

        String body = be.getText();
        List<Action> applicableActionList = actionListStack.peek();

        if (body != null) {
            body = body.trim();
            if (body.length() > 0) {
                // System.out.println("calling body method with ["+body+ "]");
                callBodyAction(applicableActionList, body);
            }
        }
    }

    public void endElement(EndEvent endEvent) {
        setDocumentLocator(endEvent.locator);
        endElement(endEvent.namespaceURI, endEvent.localName, endEvent.qName);
    }

    private void endElement(String namespaceURI, String localName, String qName) {
        // given that an action list is always pushed for every startElement, we
        // need
        // to always pop for every endElement
        List<Action> applicableActionList = (List<Action>) actionListStack.pop();

        if (skip != null) {
            if (skip.equals(elementPath)) {
                skip = null;
            }
        } else if (applicableActionList != EMPTY_LIST) {
            callEndAction(applicableActionList, getTagName(localName, qName));
        }

        // given that we always push, we must also pop the pattern
        elementPath.pop();
    }

    public Locator getLocator() {
        return locator;
    }

    public void setDocumentLocator(Locator l) {
        locator = l;
    }

    String getTagName(String localName, String qName) {
        String tagName = localName;

        if ((tagName == null) || (tagName.length() < 1)) {
            tagName = qName;
        }

        return tagName;
    }

    public void addImplicitAction(ImplicitAction ia) {
        implicitActions.add(ia);
    }

    /**
     * Check if any implicit actions are applicable. As soon as an applicable action is found, it is returned. Thus, the
     * returned list will have at most one element.
     */
    List<Action> lookupImplicitAction(ElementPath elementPath, Attributes attributes, InterpretationContext ec) {
        int len = implicitActions.size();

        for (int i = 0; i < len; i++) {
            ImplicitAction ia = (ImplicitAction) implicitActions.get(i);

            if (ia.isApplicable(elementPath, attributes, ec)) {
                List<Action> actionList = new ArrayList<Action>(1);
                actionList.add(ia);

                return actionList;
            }
        }

        return null;
    }

    /**
     * Return the list of applicable patterns for this
     */
    List<Action> getApplicableActionList(ElementPath elementPath, Attributes attributes) {
        List<Action> applicableActionList = ruleStore.matchActions(elementPath);

        // logger.debug("set of applicable patterns: " + applicableActionList);
        if (applicableActionList == null) {
            applicableActionList = lookupImplicitAction(elementPath, attributes, interpretationContext);
        }

        return applicableActionList;
    }

    void callBeginAction(List<Action> applicableActionList, String tagName, Attributes atts) {
        if (applicableActionList == null) {
            return;
        }

        Iterator<Action> i = applicableActionList.iterator();
        while (i.hasNext()) {
            Action action = (Action) i.next();
            // now let us invoke the action. We catch and report any eventual
            // exceptions
            try {
                action.begin(interpretationContext, tagName, atts);
            } catch (ActionException e) {
                skip = elementPath.duplicate();
                System.err.println("ActionException in Action for tag [" + tagName + "]");
            } catch (RuntimeException e) {
                skip = elementPath.duplicate();
                System.err.println("RuntimeException in Action for tag [" + tagName + "]");
            }
        }
    }

    private void callBodyAction(List<Action> applicableActionList, String body) {
        if (applicableActionList == null) {
            return;
        }
        Iterator<Action> i = applicableActionList.iterator();

        while (i.hasNext()) {
            Action action = i.next();
            try {
                action.body(interpretationContext, body);
            } catch (ActionException ae) {
                System.err.println("Exception in end() methd for action [" + action + "]");
            }
        }
    }

    private void callEndAction(List<Action> applicableActionList, String tagName) {
        if (applicableActionList == null) {
            return;
        }

        // logger.debug("About to call end actions on node: [" + localName + "]");
        Iterator<Action> i = applicableActionList.iterator();

        while (i.hasNext()) {
            Action action = i.next();
            // now let us invoke the end method of the action. We catch and report
            // any eventual exceptions
            try {
                action.end(interpretationContext, tagName);
            } catch (ActionException ae) {
                System.err.println("ActionException in Action for tag [" + tagName + "]");
            } catch (RuntimeException e) {
                // no point in setting skip
                System.err.println("RuntimeException in Action for tag [" + tagName + "]");
            }
        }
    }

    public RuleStore getRuleStore() {
        return ruleStore;
    }
}
