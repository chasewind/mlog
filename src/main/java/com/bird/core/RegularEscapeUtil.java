package com.bird.core;

public class RegularEscapeUtil implements IEscapeUtil {

    public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
        if (escapeChars.indexOf(next) >= 0) {
            buf.append(next);
        } else switch (next) {
            case '_':
                // the \_ sequence is swallowed
                break;
            case '\\':
                buf.append(next);
                break;
            case 't':
                buf.append('\t');
                break;
            case 'r':
                buf.append('\r');
                break;
            case 'n':
                buf.append('\n');
                break;
            default:
                String commaSeperatedEscapeChars = formatEscapeCharsForListing(escapeChars);
                throw new IllegalArgumentException("Illegal char '" + next + " at column " + pointer
                                                   + ". Only \\\\, \\_" + commaSeperatedEscapeChars
                                                   + ", \\t, \\n, \\r combinations are allowed as escape characters.");
        }
    }

    String formatEscapeCharsForListing(String escapeChars) {
        StringBuilder commaSeperatedEscapeChars = new StringBuilder();
        for (int i = 0; i < escapeChars.length(); i++) {
            commaSeperatedEscapeChars.append(", \\").append(escapeChars.charAt(i));
        }
        return commaSeperatedEscapeChars.toString();
    }

    public static String basicEscape(String s) {
        char c;
        int len = s.length();
        StringBuilder sbuf = new StringBuilder(len);

        int i = 0;
        while (i < len) {
            c = s.charAt(i++);
            if (c == '\\') {
                c = s.charAt(i++);
                if (c == 'n') {
                    c = '\n';
                } else if (c == 'r') {
                    c = '\r';
                } else if (c == 't') {
                    c = '\t';
                } else if (c == 'f') {
                    c = '\f';
                } else if (c == '\b') {
                    c = '\b';
                } else if (c == '\"') {
                    c = '\"';
                } else if (c == '\'') {
                    c = '\'';
                } else if (c == '\\') {
                    c = '\\';
                }
            }
            sbuf.append(c);
        }
        return sbuf.toString();
    }
}
