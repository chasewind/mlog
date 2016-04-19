package com.bird.core.exceptions;

public class RolloverFailure extends RuntimeException {

    private static final long serialVersionUID = -799956346239073266L;

    public RolloverFailure(String msg){
        super(msg);
    }

    public RolloverFailure(String msg, Throwable nested){
        super(msg, nested);
    }

}
