package com.bird.core.parser;

import com.bird.core.parser.Converter;

public final class LiteralConverter<E> extends Converter<E> {

    String literal;

    public LiteralConverter(String literal){
        this.literal = literal;
    }

    public String convert(E o) {
        return literal;
    }

}
