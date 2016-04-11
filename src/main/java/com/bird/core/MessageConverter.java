package com.bird.core;

public class MessageConverter extends ClassicConverter {

    public String convert(ILoggingEvent event) {
        return event.getFormattedMessage();
    }

}
