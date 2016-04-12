package com.bird.core;

public interface ContextAware {

    void setContext(Context context);

    Context getContext();

    void addError(String string, Exception e);
}
