package com.bird.core;

import com.bird.core.parser.ClassicConverter;

public abstract class ThrowableHandlingConverter extends ClassicConverter {

    boolean handlesThrowable() {
        return true;
    }
}
