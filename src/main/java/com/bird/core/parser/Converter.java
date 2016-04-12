package com.bird.core.parser;

/**
 * 类Converter.java的实现描述：TODO 转换器抽象类，打印当前事件，并将各个转换器构成链表结构
 * 
 * @author dongwei.ydw 2016年4月11日 下午1:56:36
 */
public abstract class Converter<E> {

    Converter<E> next;

    public abstract String convert(E event);

    public void write(StringBuilder buf, E event) {
        buf.append(convert(event));
    }

    public final void setNext(Converter<E> next) {
        if (this.next != null) {
            throw new IllegalStateException("Next converter has been already set");
        }
        this.next = next;
    }

    public final Converter<E> getNext() {
        return next;
    }
}
