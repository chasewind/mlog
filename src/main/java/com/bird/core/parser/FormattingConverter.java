package com.bird.core.parser;

import com.bird.core.SpacePadder;

/**
 * 类FormattingConverter.java的实现描述：TODO 类实现描述:信息格式化
 * 
 * @author dongwei.ydw 2016年4月11日 下午2:02:35
 */
public abstract class FormattingConverter<E> extends Converter<E> {

    // 缓存初始大小
    static final int INITIAL_BUF_SIZE = 256;
    static final int MAX_CAPACITY     = 1024;

    FormatInfo       formattingInfo;

    final public FormatInfo getFormattingInfo() {
        return formattingInfo;
    }

    final public void setFormattingInfo(FormatInfo formattingInfo) {
        if (this.formattingInfo != null) {
            throw new IllegalStateException("FormattingInfo has been already set");
        }
        this.formattingInfo = formattingInfo;
    }

    @Override
    final public void write(StringBuilder buf, E event) {
        String s = convert(event);

        if (formattingInfo == null) {
            buf.append(s);
            return;
        }

        int min = formattingInfo.getMin();
        int max = formattingInfo.getMax();

        if (s == null) {
            if (0 < min) SpacePadder.spacePad(buf, min);
            return;
        }

        int len = s.length();

        if (len > max) {
            if (formattingInfo.isLeftTruncate()) {
                buf.append(s.substring(len - max));
            } else {
                buf.append(s.substring(0, max));
            }
        } else if (len < min) {
            if (formattingInfo.isLeftPad()) {
                SpacePadder.leftPad(buf, s, min);
            } else {
                SpacePadder.rightPad(buf, s, min);
            }
        } else {
            buf.append(s);
        }
    }
}
