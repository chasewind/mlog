package com.bird.core.parser;

import java.util.List;

import com.bird.core.Context;
import com.bird.core.ContextAware;
import com.bird.core.ContextAwareBase;
import com.bird.core.LifeCycle;

abstract public class DynamicConverter<E> extends FormattingConverter<E> implements LifeCycle, ContextAware {

    ContextAwareBase     cab     = new ContextAwareBase(this);

    // Contains a list of option Strings.
    private List<String> optionList;

    /**
     * Is this component active?
     */
    protected boolean    started = false;

    /**
     * Components that depend on options passed during configuration can override this method in order to make
     * appropriate use of those options. For simpler components, the trivial implementation found in this abstract class
     * will be sufficient.
     */
    public void start() {
        started = true;
    }

    public void stop() {
        started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }

    /**
     * Return the first option passed to this component. The returned value may be null if there are no options.
     * 
     * @return First option, may be null.
     */
    public String getFirstOption() {
        if (optionList == null || optionList.size() == 0) {
            return null;
        } else {
            return optionList.get(0);
        }
    }

    protected List<String> getOptionList() {
        return optionList;
    }

    public void setContext(Context context) {
        cab.setContext(context);
    }

    public Context getContext() {
        return cab.getContext();
    }

    public void addError(String msg) {
        cab.addError(msg);
    }

    @Override
    public void addError(String string, Exception e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addError(String string, Throwable t) {
        // TODO Auto-generated method stub

    }

}
