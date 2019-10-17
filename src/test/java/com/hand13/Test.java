package com.hand13;

import java.io.StringBufferInputStream;

public class Test {
    public static void main(String[] args) {
        ListParser parser = new ListParser(new StringBufferInputStream("(+ 12 (+ 100 100) 12 (car '( 13 15 ) ) )"));
        Object o = parser.getNextObject();
        System.out.println(o);
    }
}
