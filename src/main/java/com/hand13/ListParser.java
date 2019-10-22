package com.hand13;

import java.io.InputStream;

import static com.hand13.Token.*;

public class ListParser {
    private TokenStream ts;
    private final String[] inners = {
            "lambda", "define","if","begin"
    };

    public ListParser(InputStream in) {
        ts = new TokenStream(in);
    }

    public List getNextList() {
        String token = ts.getNextToken();
        if (token == null || !token.equals("(")) {
            throw new RuntimeException("error parse");
        }
        token = ts.getNextToken();
        if (token == null) {
            throw new RuntimeException("error parse");
        }
        if (token.equals(")")) {
            return null;
        }
        ts.back();
        List header = new List(getNextObject(), null);
        List tmp = header;
        Object o;
        while ((o = getNextObject()) != null) {
            tmp.snd = new List(o, null);
            tmp = (List) tmp.snd;
        }
        String t = ts.getCurrentToken();
        if (t == null) {
            throw new RuntimeException("parse error");
        }
        return header;
    }

    public boolean isInner(String token) {
        boolean result = false;
        for (String inn : inners) {
            if (inn.equals(token)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public Object getNextObject() {
        String token = ts.getNextToken();
        if (token == null || token.equals(")")) {
            return null;
        }
        if (token.equals("(")) {
            ts.back();
            return getNextList();
        }
        if (token.equals("'")) {
            return new QuotedObject(getNextObject());
        }
        //字符串
        if (token.startsWith("\"")) {
            return token.substring(1, token.length() - 1);
        }
        if (Character.isDigit(token.charAt(0))) {
            return Double.valueOf(token);
        }
        if (isInner(token)) {
            return string2Token(token);
        }
        if (token.startsWith("#")) {
            return string2SpecialValue(token);
        }
        return new Symbol(token);
    }

    private Token string2Token(String value) {
        switch (value) {
            case "lambda":
                return LAMBDA;
            case "define":
                return DEFINE;
            case "if":
                return IF;
            case "begin":
                return BEGIN;
            default:
                return null;
        }
    }

    private Object string2SpecialValue(String value) {
        if (value == null) {
            return null;
        }
        if(value.equals("#t")) {
            return true;
        }else if(value.equals("#f")) {
            return false;
        }
        if (value.startsWith("#\\")) {
            String ch = value.substring(2);
            return s2c(ch);
        }
        return null;
    }

    private Character s2c(String value) {
        Character result = null;
        if (value != null) {
            if (value.length() == 1) {
                result = value.charAt(0);
            }else {
                result = mc2c(value);
            }
        }
        return result;
    }
    private Character mc2c(String value) {
        if(value.equals("newline")) {
            return '\n';
        }
        return null;
    }
}
