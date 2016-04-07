package com.bird.core;

public interface PostCompileProcessor<E> {

    void process(Context context, Converter<E> head);
}
