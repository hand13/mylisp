package com.hand13;

import java.io.StringBufferInputStream;
import java.util.HashMap;

public class Lisp {
    public static void main(String[] args) {
        Env env = new Env(new HashMap<String, Object>(),null);
        LispBase.initEnv(env);
        ListParser parser = new ListParser(new StringBufferInputStream("(+ 12 (+ 100 100) (+ 13 13) (car (cdr ' (13 17 ) ) ) )"));
        Object o  = parser.getNextObject();
        Object r = LispBase.eval(o,env);
        System.out.println(r);
    }
}
