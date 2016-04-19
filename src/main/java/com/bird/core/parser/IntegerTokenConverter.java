package com.bird.core.parser;

public class IntegerTokenConverter extends DynamicConverter<Object> implements MonoTypedConverter {

    public final static String CONVERTER_KEY = "i";

    public String convert(int i) {
        return Integer.toString(i);
    }

    public String convert(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Null argument forbidden");
        }
        if (o instanceof Integer) {
            Integer i = (Integer) o;
            return convert(i.intValue());
        }
        throw new IllegalArgumentException("Cannot convert " + o + " of type" + o.getClass().getName());
    }

    public boolean isApplicable(Object o) {
        return (o instanceof Integer);
    }
}
