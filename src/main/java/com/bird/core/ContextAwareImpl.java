package com.bird.core;

public class ContextAwareImpl implements ContextAware {

    protected Context context;
    final Object      origin;

    public ContextAwareImpl(Context context, Object origin){
        this.context = context;
        this.origin = origin;

    }

    protected Object getOrigin() {
        return origin;
    }

    public void setContext(Context context) {
        if (this.context == null) {
            this.context = context;
        } else if (this.context != context) {
            throw new IllegalStateException("Context has been already set");
        }
    }

    public Context getContext() {
        return this.context;
    }

    @Override
    public void addError(String string, Exception e) {
        // TODO Auto-generated method stub

    }

}
