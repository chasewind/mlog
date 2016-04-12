package com.bird.core.exceptions;

public class JoranException extends Exception {

    private static final long serialVersionUID = 1112493363728774021L;

    public JoranException(String msg){
        super(msg);
    }

    public JoranException(String msg, Throwable cause){
        super(msg, cause);
    }
}
