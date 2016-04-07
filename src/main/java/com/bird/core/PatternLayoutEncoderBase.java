package com.bird.core;

public class PatternLayoutEncoderBase<E> extends LayoutWrappingEncoder<E> {

    String            pattern;

    // due to popular demand outputPatternAsHeader is set to false by default
    protected boolean outputPatternAsHeader = false;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isOutputPatternAsHeader() {
        return outputPatternAsHeader;
    }

    /**
     * Print the pattern string as a header in log files
     *
     * @param outputPatternAsHeader
     * @since 1.0.3
     */
    public void setOutputPatternAsHeader(boolean outputPatternAsHeader) {
        this.outputPatternAsHeader = outputPatternAsHeader;
    }

    public boolean isOutputPatternAsPresentationHeader() {
        return outputPatternAsHeader;
    }

    @Override
    public void setLayout(Layout<E> layout) {
        throw new UnsupportedOperationException("one cannot set the layout of " + this.getClass().getName());
    }

}
