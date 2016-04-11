package com.bird.core;

public class ThrowableProxyUtil {

    public static final int    REGULAR_EXCEPTION_INDENT    = 1;
    public static final int    SUPPRESSED_EXCEPTION_INDENT = 1;
    private static final int   BUILDER_CAPACITY            = 2048;
    public static final String LINE_SEPARATOR              = System.getProperty("line.separator");
    public static final String WRAPPED_BY                  = "Wrapped by: ";
    public static final String CAUSED_BY                   = "Caused by: ";
    public static final String SUPPRESSED                  = "Suppressed: ";
    public static final char   TAB                         = '\t';

    public static void build(ThrowableProxy nestedTP, Throwable nestedThrowable, ThrowableProxy parentTP) {

        StackTraceElement[] nestedSTE = nestedThrowable.getStackTrace();

        int commonFramesCount = -1;
        if (parentTP != null) {
            commonFramesCount = findNumberOfCommonFrames(nestedSTE, parentTP.getStackTraceElementProxyArray());
        }

        nestedTP.commonFrames = commonFramesCount;
        nestedTP.stackTraceElementProxyArray = steArrayToStepArray(nestedSTE);
    }

    static StackTraceElementProxy[] steArrayToStepArray(StackTraceElement[] stea) {
        if (stea == null) {
            return new StackTraceElementProxy[0];
        }
        StackTraceElementProxy[] stepa = new StackTraceElementProxy[stea.length];
        for (int i = 0; i < stepa.length; i++) {
            stepa[i] = new StackTraceElementProxy(stea[i]);
        }
        return stepa;
    }

    static int findNumberOfCommonFrames(StackTraceElement[] steArray, StackTraceElementProxy[] parentSTEPArray) {
        if (parentSTEPArray == null || steArray == null) {
            return 0;
        }

        int steIndex = steArray.length - 1;
        int parentIndex = parentSTEPArray.length - 1;
        int count = 0;
        while (steIndex >= 0 && parentIndex >= 0) {
            StackTraceElement ste = steArray[steIndex];
            StackTraceElement otherSte = parentSTEPArray[parentIndex].ste;
            if (ste.equals(otherSte)) {
                count++;
            } else {
                break;
            }
            steIndex--;
            parentIndex--;
        }
        return count;
    }

    public static String asString(IThrowableProxy tp) {
        StringBuilder sb = new StringBuilder(BUILDER_CAPACITY);

        recursiveAppend(sb, null, REGULAR_EXCEPTION_INDENT, tp);

        return sb.toString();
    }

    private static void recursiveAppend(StringBuilder sb, String prefix, int indent, IThrowableProxy tp) {
        if (tp == null) return;
        subjoinFirstLine(sb, prefix, indent, tp);
        sb.append(LINE_SEPARATOR);
        subjoinSTEPArray(sb, indent, tp);
        IThrowableProxy[] suppressed = tp.getSuppressed();
        if (suppressed != null) {
            for (IThrowableProxy current : suppressed) {
                recursiveAppend(sb, SUPPRESSED, indent + SUPPRESSED_EXCEPTION_INDENT, current);
            }
        }
        recursiveAppend(sb, CAUSED_BY, indent, tp.getCause());
    }

    public static void indent(StringBuilder buf, int indent) {
        for (int j = 0; j < indent; j++) {
            buf.append(TAB);
        }
    }

    private static void subjoinFirstLine(StringBuilder buf, String prefix, int indent, IThrowableProxy tp) {
        indent(buf, indent - 1);
        if (prefix != null) {
            buf.append(prefix);
        }
        subjoinExceptionMessage(buf, tp);
    }

    public static void subjoinPackagingData(StringBuilder builder, StackTraceElementProxy step) {
        if (step != null) {
            ClassPackagingData cpd = step.getClassPackagingData();
            if (cpd != null) {
                if (!cpd.isExact()) {
                    builder.append(" ~[");
                } else {
                    builder.append(" [");
                }

                builder.append(cpd.getCodeLocation()).append(':').append(cpd.getVersion()).append(']');
            }
        }
    }

    public static void subjoinSTEP(StringBuilder sb, StackTraceElementProxy step) {
        sb.append(step.toString());
        subjoinPackagingData(sb, step);
    }

    /**
     * @param sb The StringBuilder the STEPs are appended to.
     * @param indentLevel indentation level used for the STEPs, usually REGULAR_EXCEPTION_INDENT.
     * @param tp the IThrowableProxy containing the STEPs.
     */
    public static void subjoinSTEPArray(StringBuilder sb, int indentLevel, IThrowableProxy tp) {
        StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
        int commonFrames = tp.getCommonFrames();

        for (int i = 0; i < stepArray.length - commonFrames; i++) {
            StackTraceElementProxy step = stepArray[i];
            indent(sb, indentLevel);
            subjoinSTEP(sb, step);
            sb.append(LINE_SEPARATOR);
        }

        if (commonFrames > 0) {
            indent(sb, indentLevel);
            sb.append("... ").append(commonFrames).append(" common frames omitted").append(LINE_SEPARATOR);
        }

    }

    public static void subjoinFirstLine(StringBuilder buf, IThrowableProxy tp) {
        int commonFrames = tp.getCommonFrames();
        if (commonFrames > 0) {
            buf.append(CAUSED_BY);
        }
        subjoinExceptionMessage(buf, tp);
    }

    public static void subjoinFirstLineRootCauseFirst(StringBuilder buf, IThrowableProxy tp) {
        if (tp.getCause() != null) {
            buf.append(WRAPPED_BY);
        }
        subjoinExceptionMessage(buf, tp);
    }

    private static void subjoinExceptionMessage(StringBuilder buf, IThrowableProxy tp) {
        buf.append(tp.getClassName()).append(": ").append(tp.getMessage());
    }
}
