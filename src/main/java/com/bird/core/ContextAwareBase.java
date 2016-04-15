package com.bird.core;

public class ContextAwareBase implements ContextAware {

    final Object declaredOrigin;

    public ContextAwareBase(){
        declaredOrigin = this;
    }

    public ContextAwareBase(ContextAware declaredOrigin){
        this.declaredOrigin = declaredOrigin;
    }

    protected Context context;

    @Override
    public void setContext(Context context) {
        if (this.context == null) {
            this.context = context;
        } else if (this.context != context) {
            throw new IllegalStateException("Context has been already set");
        }
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    public void addInfo(String msg) {
        System.err.println("info---->" + msg);
    }

    public void addError(String msg) {
        System.err.println("error---->" + msg);
    }

    public void addError(String msg, Exception e) {
        System.err.println("error---->" + msg + ",<---" + e.getMessage());
    }

    @Override
    public void addError(String string, Throwable t) {
        // TODO Auto-generated method stub

    }
}
