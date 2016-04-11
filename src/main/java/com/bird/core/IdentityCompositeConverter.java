package com.bird.core;

public class IdentityCompositeConverter<E> extends CompositeConverter<E> {

    @Override
    protected String transform(E event, String in) {
        return in;
    }
}
