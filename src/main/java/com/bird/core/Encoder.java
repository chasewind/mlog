package com.bird.core;

import java.io.IOException;
import java.io.OutputStream;

public interface Encoder<E> extends ContextAware, LifeCycle {

    void init(OutputStream os) throws IOException;

    void doEncode(E event) throws IOException;

    void close() throws IOException;
}
