package com.bird.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class LayoutWrappingEncoder<E> extends EncoderBase<E> {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    protected Layout<E>        layout;

    private Charset            charset;

    private boolean            immediateFlush = true;

    public void setImmediateFlush(boolean immediateFlush) {
        this.immediateFlush = immediateFlush;
    }

    public boolean isImmediateFlush() {
        return immediateFlush;
    }

    public Layout<E> getLayout() {
        return layout;
    }

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

    public Charset getCharset() {
        return charset;
    }

    /**
     * Set the charset to use when converting the string returned by the layout into bytes.
     * <p/>
     * By default this property has the value <code>null</null> which corresponds to the system's default charset.
     *
     * @param charset
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void init(OutputStream os) throws IOException {
        super.init(os);
        writeHeader();
    }

    void writeHeader() throws IOException {
        if (layout != null && (outputStream != null)) {
            StringBuilder sb = new StringBuilder();
            appendIfNotNull(sb, layout.getFileHeader());
            appendIfNotNull(sb, layout.getPresentationHeader());
            if (sb.length() > 0) {
                sb.append(LINE_SEPARATOR);
                outputStream.write(convertToBytes(sb.toString()));
                outputStream.flush();
            }
        }
    }

    public void close() throws IOException {
        writeFooter();
    }

    void writeFooter() throws IOException {
        if (layout != null && outputStream != null) {
            StringBuilder sb = new StringBuilder();
            appendIfNotNull(sb, layout.getPresentationFooter());
            appendIfNotNull(sb, layout.getFileFooter());
            if (sb.length() > 0) {
                outputStream.write(convertToBytes(sb.toString()));
                outputStream.flush();
            }
        }
    }

    private byte[] convertToBytes(String s) {
        if (charset == null) {
            return s.getBytes();
        } else {
            try {
                return s.getBytes(charset.name());
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException("An existing charset cannot possibly be unsupported.");
            }
        }
    }

    public void doEncode(E event) throws IOException {
        String txt = layout.doLayout(event);
        outputStream.write(convertToBytes(txt));
        if (immediateFlush) outputStream.flush();
    }

    public boolean isStarted() {
        return false;
    }

    public void start() {
        started = true;
    }

    public void stop() {
        started = false;
        if (outputStream != null) {
            try {
                outputStream.flush();
            } catch (IOException e) {
            }
        }
    }

    private void appendIfNotNull(StringBuilder sb, String s) {
        if (s != null) {
            sb.append(s);
        }
    }

}
