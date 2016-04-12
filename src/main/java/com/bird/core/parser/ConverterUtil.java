package com.bird.core.parser;

import com.bird.core.Context;
import com.bird.core.ContextAware;

public class ConverterUtil {

    /**
     * Start converters in the chain of converters.
     *
     * @param head
     */
    public static <E> void startConverters(Converter<E> head) {
        Converter<E> c = head;
        while (c != null) {
            // CompositeConverter is a subclass of DynamicConverter
            if (c instanceof CompositeConverter) {
                CompositeConverter<E> cc = (CompositeConverter<E>) c;
                Converter<E> childConverter = cc.childConverter;
                startConverters(childConverter);
                cc.start();
            } else if (c instanceof DynamicConverter) {
                DynamicConverter<E> dc = (DynamicConverter<E>) c;
                dc.start();
            }
            c = c.getNext();
        }
    }

    public static <E> Converter<E> findTail(Converter<E> head) {
        Converter<E> p = head;
        while (p != null) {
            Converter<E> next = p.getNext();
            if (next == null) {
                break;
            } else {
                p = next;
            }
        }
        return p;
    }

    public static <E> void setContextForConverters(Context context, Converter<E> head) {
        Converter<E> c = head;
        while (c != null) {
            if (c instanceof ContextAware) {
                ((ContextAware) c).setContext(context);
            }
            c = c.getNext();
        }
    }
}
