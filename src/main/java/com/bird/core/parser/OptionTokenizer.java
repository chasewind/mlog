package com.bird.core.parser;

import java.util.ArrayList;
import java.util.List;

import com.bird.core.CoreConstants;
import com.bird.core.exceptions.ScanException;
import com.bird.core.parser.TokenStream.TokenizerState;

public class OptionTokenizer {

    private final static int EXPECTING_STATE         = 0;
    private final static int RAW_COLLECTING_STATE    = 1;
    private final static int QUOTED_COLLECTING_STATE = 2;

    final IEscapeUtil        escapeUtil;
    final TokenStream        tokenStream;
    final String             pattern;
    final int                patternLength;

    char                     quoteChar;
    int                      state                   = EXPECTING_STATE;

    OptionTokenizer(TokenStream tokenStream){
        this(tokenStream, new AsIsEscapeUtil());
    }

    OptionTokenizer(TokenStream tokenStream, IEscapeUtil escapeUtil){
        this.tokenStream = tokenStream;
        this.pattern = tokenStream.pattern;
        this.patternLength = tokenStream.patternLength;
        this.escapeUtil = escapeUtil;
    }

    void tokenize(char firstChar, List<Token> tokenList) throws ScanException {
        StringBuffer buf = new StringBuffer();
        List<String> optionList = new ArrayList<String>();
        char c = firstChar;

        while (tokenStream.pointer < patternLength) {
            switch (state) {
                case EXPECTING_STATE:
                    switch (c) {
                        case ' ':
                        case '\t':
                        case '\r':
                        case '\n':
                        case CoreConstants.COMMA_CHAR:
                            break;
                        case CoreConstants.SINGLE_QUOTE_CHAR:
                        case CoreConstants.DOUBLE_QUOTE_CHAR:
                            state = QUOTED_COLLECTING_STATE;
                            quoteChar = c;
                            break;
                        case CoreConstants.CURLY_RIGHT:
                            emitOptionToken(tokenList, optionList);
                            return;
                        default:
                            buf.append(c);
                            state = RAW_COLLECTING_STATE;
                    }
                    break;
                case RAW_COLLECTING_STATE:
                    switch (c) {
                        case CoreConstants.COMMA_CHAR:
                            optionList.add(buf.toString().trim());
                            buf.setLength(0);
                            state = EXPECTING_STATE;
                            break;
                        case CoreConstants.CURLY_RIGHT:
                            optionList.add(buf.toString().trim());
                            emitOptionToken(tokenList, optionList);
                            return;
                        default:
                            buf.append(c);
                    }
                    break;
                case QUOTED_COLLECTING_STATE:
                    if (c == quoteChar) {
                        optionList.add(buf.toString());
                        buf.setLength(0);
                        state = EXPECTING_STATE;
                    } else if (c == CoreConstants.ESCAPE_CHAR) {
                        escape(String.valueOf(quoteChar), buf);
                    } else {
                        buf.append(c);
                    }

                    break;
            }

            c = pattern.charAt(tokenStream.pointer);
            tokenStream.pointer++;
        }

        // EOS
        if (c == CoreConstants.CURLY_RIGHT) {
            if (state == EXPECTING_STATE) {
                emitOptionToken(tokenList, optionList);
            } else if (state == RAW_COLLECTING_STATE) {
                optionList.add(buf.toString().trim());
                emitOptionToken(tokenList, optionList);
            } else {
                throw new ScanException("Unexpected end of pattern string in OptionTokenizer");
            }
        } else {
            throw new ScanException("Unexpected end of pattern string in OptionTokenizer");
        }
    }

    void emitOptionToken(List<Token> tokenList, List<String> optionList) {
        tokenList.add(new Token(Token.OPTION, optionList));
        tokenStream.state = TokenizerState.LITERAL_STATE;
    }

    void escape(String escapeChars, StringBuffer buf) {
        if ((tokenStream.pointer < patternLength)) {
            char next = pattern.charAt(tokenStream.pointer++);
            escapeUtil.escape(escapeChars, buf, next, tokenStream.pointer);
        }
    }
}
