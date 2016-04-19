package com.bird.core.helpers;

import com.bird.core.CoreConstants;
import com.bird.core.parser.RestrictedEscapeUtil;

public class AlmostAsIsEscapeUtil extends RestrictedEscapeUtil {

    public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
        super.escape("" + CoreConstants.PERCENT_CHAR + CoreConstants.RIGHT_PARENTHESIS_CHAR, buf, next, pointer);
    }
}
