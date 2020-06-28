package com.hand13;

import java.io.StringBufferInputStream;

public class Test {
    public static void main(String[] args) {
        ListParser parser = new ListParser(new StringBufferInputStream("(define a 12)"));
        Object o = parser.getNextObject();
        System.out.println(o);
    }
}
