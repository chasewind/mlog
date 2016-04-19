package com.bird.core;

import java.util.List;
import java.util.regex.Pattern;

import com.bird.core.parser.CompositeConverter;

/**
 * 类ReplacingCompositeConverter.java的实现描述：TODO 类实现描述
 * 
 * @author dongwei.ydw 2016年4月19日 下午3:40:43
 */
public class ReplacingCompositeConverter<E> extends CompositeConverter<E> {

    Pattern pattern;
    String  regex;
    String  replacement;

    public void start() {
        final List<String> optionList = getOptionList();
        if (optionList == null) {
            addError("at least two options are expected whereas you have declared none");
            return;
        }

        int numOpts = optionList.size();

        if (numOpts < 2) {
            addError("at least two options are expected whereas you have declared only " + numOpts + "as [" + optionList
                     + "]");
            return;
        }
        regex = optionList.get(0);
        pattern = Pattern.compile(regex);
        replacement = optionList.get(1);
        super.start();
    }

    @Override
    protected String transform(E event, String in) {
        if (!started) return in;
        return pattern.matcher(in).replaceAll(replacement);
    }
}
