package com.hand13;

import java.io.StringBufferInputStream;

public class Test {
    public static void main(String[] args) {
        ListParser parser = new ListParser(new StringBufferInputStream("(1 2 3 4 5 6)"));
        Object o = parser.getNextObject();
        System.out.println(o);
    }
}
