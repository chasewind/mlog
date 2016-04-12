package com.bird.core.layout;

import java.util.HashMap;
import java.util.Map;

import com.bird.core.EnsureExceptionHandling;
import com.bird.core.FileOfCallerConverter;
import com.bird.core.ILoggingEvent;
import com.bird.core.LevelConverter;
import com.bird.core.LineOfCallerConverter;
import com.bird.core.LineSeparatorConverter;
import com.bird.core.LoggerConverter;
import com.bird.core.MessageConverter;
import com.bird.core.MethodOfCallerConverter;
import com.bird.core.PropertyConverter;
import com.bird.core.RelativeTimeConverter;
import com.bird.core.RootCauseFirstThrowableProxyConverter;
import com.bird.core.ThreadConverter;
import com.bird.core.ThrowableProxyConverter;
import com.bird.core.parser.CallerDataConverter;
import com.bird.core.parser.ClassOfCallerConverter;
import com.bird.core.parser.ContextNameConverter;
import com.bird.core.parser.DateConverter;
import com.bird.core.parser.Parser;

public class PatternLayout extends PatternLayoutBase<ILoggingEvent> {

    public static final Map<String, String> defaultConverterMap = new HashMap<String, String>();
    public static final String              HEADER_PREFIX       = "#logback.classic pattern: ";

    static {
        defaultConverterMap.putAll(Parser.DEFAULT_COMPOSITE_CONVERTER_MAP);

        defaultConverterMap.put("d", DateConverter.class.getName());
        defaultConverterMap.put("date", DateConverter.class.getName());

        defaultConverterMap.put("r", RelativeTimeConverter.class.getName());
        defaultConverterMap.put("relative", RelativeTimeConverter.class.getName());

        defaultConverterMap.put("level", LevelConverter.class.getName());
        defaultConverterMap.put("le", LevelConverter.class.getName());
        defaultConverterMap.put("p", LevelConverter.class.getName());

        defaultConverterMap.put("t", ThreadConverter.class.getName());
        defaultConverterMap.put("thread", ThreadConverter.class.getName());

        defaultConverterMap.put("lo", LoggerConverter.class.getName());
        defaultConverterMap.put("logger", LoggerConverter.class.getName());
        defaultConverterMap.put("c", LoggerConverter.class.getName());

        defaultConverterMap.put("m", MessageConverter.class.getName());
        defaultConverterMap.put("msg", MessageConverter.class.getName());
        defaultConverterMap.put("message", MessageConverter.class.getName());

        defaultConverterMap.put("C", ClassOfCallerConverter.class.getName());
        defaultConverterMap.put("class", ClassOfCallerConverter.class.getName());

        defaultConverterMap.put("M", MethodOfCallerConverter.class.getName());
        defaultConverterMap.put("method", MethodOfCallerConverter.class.getName());

        defaultConverterMap.put("L", LineOfCallerConverter.class.getName());
        defaultConverterMap.put("line", LineOfCallerConverter.class.getName());

        defaultConverterMap.put("F", FileOfCallerConverter.class.getName());
        defaultConverterMap.put("file", FileOfCallerConverter.class.getName());

        defaultConverterMap.put("ex", ThrowableProxyConverter.class.getName());
        defaultConverterMap.put("exception", ThrowableProxyConverter.class.getName());
        defaultConverterMap.put("rEx", RootCauseFirstThrowableProxyConverter.class.getName());
        defaultConverterMap.put("rootException", RootCauseFirstThrowableProxyConverter.class.getName());
        defaultConverterMap.put("throwable", ThrowableProxyConverter.class.getName());

        defaultConverterMap.put("cn", ContextNameConverter.class.getName());
        defaultConverterMap.put("contextName", ContextNameConverter.class.getName());

        defaultConverterMap.put("caller", CallerDataConverter.class.getName());

        defaultConverterMap.put("property", PropertyConverter.class.getName());

        defaultConverterMap.put("n", LineSeparatorConverter.class.getName());

    }

    public PatternLayout(){
        this.postCompileProcessor = new EnsureExceptionHandling();
    }

    public Map<String, String> getDefaultConverterMap() {
        return defaultConverterMap;
    }

    public String doLayout(ILoggingEvent event) {
        if (!isStarted()) {
            return EMPTY_STRING;
        }
        return writeLoopOnConverters(event);
    }

    @Override
    protected String getPresentationHeaderPrefix() {
        return HEADER_PREFIX;
    }
}
