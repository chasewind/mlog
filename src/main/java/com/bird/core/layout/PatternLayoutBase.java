package com.bird.core.layout;

import java.util.HashMap;
import java.util.Map;

import com.bird.core.Context;
import com.bird.core.PostCompileProcessor;
import com.bird.core.exceptions.ScanException;
import com.bird.core.parser.Converter;
import com.bird.core.parser.ConverterUtil;
import com.bird.core.parser.Node;
import com.bird.core.parser.Parser;

abstract public class PatternLayoutBase<E> extends LayoutBase<E> {

    public static final String        EMPTY_STRING          = "";
    Converter<E>                      head;
    String                            pattern;
    protected PostCompileProcessor<E> postCompileProcessor;

    Map<String, String>               instanceConverterMap  = new HashMap<String, String>();
    protected boolean                 outputPatternAsHeader = false;
    public static final String        PATTERN_RULE_REGISTRY = "PATTERN_RULE_REGISTRY";

    abstract public Map<String, String> getDefaultConverterMap();

    public Map<String, String> getEffectiveConverterMap() {
        Map<String, String> effectiveMap = new HashMap<String, String>();

        // add the least specific map fist
        Map<String, String> defaultMap = getDefaultConverterMap();
        if (defaultMap != null) {
            effectiveMap.putAll(defaultMap);
        }

        // contextMap is more specific than the default map
        Context context = getContext();
        if (context != null) {
            @SuppressWarnings("unchecked")
            Map<String, String> contextMap = (Map<String, String>) context.getObject(PATTERN_RULE_REGISTRY);
            if (contextMap != null) {
                effectiveMap.putAll(contextMap);
            }
        }
        // set the most specific map last
        effectiveMap.putAll(instanceConverterMap);
        return effectiveMap;
    }

    public void start() {
        if (pattern == null || pattern.length() == 0) {
            addError("Empty or null pattern.");
            return;
        }
        try {
            Parser<E> p = new Parser<E>(pattern);
            if (getContext() != null) {
                p.setContext(getContext());
            }
            Node t = p.parse();
            this.head = p.compile(t, getEffectiveConverterMap());
            if (postCompileProcessor != null) {
                postCompileProcessor.process(context, head);
            }
            ConverterUtil.setContextForConverters(getContext(), head);
            ConverterUtil.startConverters(this.head);
            super.start();
        } catch (ScanException sce) {
            System.err.println("Failed to parse pattern \"" + getPattern() + "\".");
        }
    }

    public void setPostCompileProcessor(PostCompileProcessor<E> postCompileProcessor) {
        this.postCompileProcessor = postCompileProcessor;
    }

    protected String writeLoopOnConverters(E event) {
        StringBuilder buf = new StringBuilder(128);
        Converter<E> c = head;
        while (c != null) {
            c.write(buf, event);
            c = c.getNext();
        }
        return buf.toString();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String toString() {
        return this.getClass().getName() + "(\"" + getPattern() + "\")";
    }

    public Map<String, String> getInstanceConverterMap() {
        return instanceConverterMap;
    }

    protected String getPresentationHeaderPrefix() {
        return EMPTY_STRING;
    }

    public boolean isOutputPatternAsHeader() {
        return outputPatternAsHeader;
    }

    public void setOutputPatternAsHeader(boolean outputPatternAsHeader) {
        this.outputPatternAsHeader = outputPatternAsHeader;
    }

    @Override
    public String getPresentationHeader() {
        if (outputPatternAsHeader) return getPresentationHeaderPrefix() + pattern;
        else return super.getPresentationHeader();
    }
}
