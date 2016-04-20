package com.bird.core.exceptions;

public class RolloverFailureException extends RuntimeException {

    private static final long serialVersionUID = -799956346239073266L;

    public RolloverFailureException(String msg){
        super(msg);
    }

    public RolloverFailureException(String msg, Throwable nested){
        super(msg, nested);
    }

}
