package com.bird.core;

import com.bird.core.parser.Converter;

public interface PostCompileProcessor<E> {

    void process(Context context, Converter<E> head);
}
