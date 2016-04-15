package com.bird.core;

import org.xml.sax.Locator;

public class EndEvent extends SaxEvent {

    public EndEvent(String namespaceURI, String localName, String qName, Locator locator){
        super(namespaceURI, localName, qName, locator);
    }

    @Override
    public String toString() {
        return "  EndEvent(" + getQName() + ")  [" + locator.getLineNumber() + "," + locator.getColumnNumber() + "]";
    }

}
