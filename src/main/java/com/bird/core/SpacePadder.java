package com.bird.core;

/**
 * 类SpacePadder.java的实现描述：TODO 类实现描述:格式化信息，通过调整空格个数，使数据格式一致
 * 
 * @author dongwei.ydw 2016年4月11日 下午2:03:49
 */
public class SpacePadder {

    final static String[] SPACES = { " ", "  ", "    ", "        ", // 1,2,4,8
                                     // spaces
                                     "                ", // 16 spaces
                                     "                                " }; // 32 spaces

    final static public void leftPad(StringBuilder buf, String s, int desiredLength) {
        int actualLen = 0;
        if (s != null) {
            actualLen = s.length();
        }
        if (actualLen < desiredLength) {
            spacePad(buf, desiredLength - actualLen);
        }
        if (s != null) {
            buf.append(s);
        }
    }

    final static public void rightPad(StringBuilder buf, String s, int desiredLength) {
        int actualLen = 0;
        if (s != null) {
            actualLen = s.length();
        }
        if (s != null) {
            buf.append(s);
        }
        if (actualLen < desiredLength) {
            spacePad(buf, desiredLength - actualLen);
        }
    }

    final static public void spacePad(StringBuilder sbuf, int length) {
        while (length >= 32) {
            sbuf.append(SPACES[5]);
            length -= 32;
        }

        for (int i = 4; i >= 0; i--) {
            if ((length & (1 << i)) != 0) {
                sbuf.append(SPACES[i]);
            }
        }
    }
}
