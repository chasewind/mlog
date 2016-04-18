package com.bird.core;

public interface ContextAware {

    void setContext(Context context);

    Context getContext();

    void addError(String msg, Exception e);

    void addError(String msg);

    void addError(String msg, Throwable t);
}
