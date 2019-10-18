package com.hand13;

import java.io.IOException;
import java.io.InputStream;

public class TokenStream {
    private InputStream in;
    private boolean back = false;
    private boolean backforward = false;

    private String currentToken;
    private char currentChar;

    public char nextChar() {
        if(backforward) {
            backforward = false;
            return currentChar;
        }
        int a = -1;
        try {
            a = in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(a <= 0) {
            a = 0;
        }
        currentChar = (char)a;
        return currentChar;
    }
    public void backforward() {
        backforward = true;
    }
    public char nextCleanChar() {
        char m = nextChar();
        if(m == ' ') {
            return nextCleanChar();
        }
        return m;
    }

    public TokenStream(InputStream in){
        this.in = in;
    }
    public String getNextToken() {
        if(back) {
            back = false;
        }else{
            getNext();
        }
        return currentToken;
    }
    private void getNext() {
        char m = this.nextCleanChar();
        if(m == 0){
            currentToken = null;
        }else if(m == '(' || m == ')' || m == '\'') {
            currentToken = String.valueOf(m);
        }else if(m == '"') {
            currentToken = getQuotedString();
        }else{
            backforward();
            currentToken = getString();
        }
    }
    public String getString() {
        StringBuilder str = new StringBuilder();
        char m = nextChar();
        while (m != '(' && m != ')' && m!= ' ' && m != 0) {
            str.append(m);
            m = nextChar();
        }
        backforward();
        return str.toString();
    }
    public String getQuotedString() {
        StringBuilder str = new StringBuilder();
        str.append('"');
        char m = nextChar();
        while (m != '"' && m != 0) {
            str.append(m);
            m = nextChar();
        }
        if(m != '"') {
            backforward();
        }
        str.append('"');
        return str.toString();
    }
    public void back() {
        back = true;
    }
    public String getCurrentToken() {
        return currentToken;
    }
}
