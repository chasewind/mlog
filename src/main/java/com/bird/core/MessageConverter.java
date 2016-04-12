package com.bird.core;

import com.bird.core.parser.ClassicConverter;

public class MessageConverter extends ClassicConverter {

    public String convert(ILoggingEvent event) {
        return event.getFormattedMessage();
    }

}
