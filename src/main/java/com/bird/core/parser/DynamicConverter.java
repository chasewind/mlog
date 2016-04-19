package com.bird.core.parser;

import java.util.List;

import com.bird.core.Context;
import com.bird.core.ContextAware;
import com.bird.core.ContextAwareBase;
import com.bird.core.LifeCycle;

/**
 * 类DynamicConverter.java的实现描述：TODO 类实现描述
 * 
 * @author dongwei.ydw 2016年4月19日 下午3:34:25
 */
abstract public class DynamicConverter<E> extends FormattingConverter<E> implements LifeCycle, ContextAware {

    ContextAwareBase     cab     = new ContextAwareBase(this);

    private List<String> optionList;

    protected boolean    started = false;

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
    public void addError(String msg, Exception e) {
        cab.addError(msg, e);

    }

    @Override
    public void addError(String msg, Throwable t) {
        cab.addError(msg, t);

    }

}
