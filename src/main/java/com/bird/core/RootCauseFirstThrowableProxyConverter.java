package com.bird.core;

public class RootCauseFirstThrowableProxyConverter extends ExtendedThrowableProxyConverter {

    @Override
    protected String throwableProxyToString(IThrowableProxy tp) {
        StringBuilder buf = new StringBuilder(BUILDER_CAPACITY);
        recursiveAppendRootCauseFirst(buf, null, ThrowableProxyUtil.REGULAR_EXCEPTION_INDENT, tp);
        return buf.toString();
    }

    protected void recursiveAppendRootCauseFirst(StringBuilder sb, String prefix, int indent, IThrowableProxy tp) {
        if (tp.getCause() != null) {
            recursiveAppendRootCauseFirst(sb, prefix, indent, tp.getCause());
            prefix = null; // to avoid adding it more than once
        }
        ThrowableProxyUtil.indent(sb, indent - 1);
        if (prefix != null) {
            sb.append(prefix);
        }
        ThrowableProxyUtil.subjoinFirstLineRootCauseFirst(sb, tp);
        sb.append(CoreConstants.LINE_SEPARATOR);
        subjoinSTEPArray(sb, indent, tp);
        IThrowableProxy[] suppressed = tp.getSuppressed();
        if (suppressed != null) {
            for (IThrowableProxy current : suppressed) {
                recursiveAppendRootCauseFirst(sb, CoreConstants.SUPPRESSED,
                                              indent + ThrowableProxyUtil.SUPPRESSED_EXCEPTION_INDENT, current);
            }
        }
    }
}
