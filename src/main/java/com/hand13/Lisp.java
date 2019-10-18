package com.hand13;

import java.io.StringBufferInputStream;
import java.util.HashMap;

public class Lisp {
    public static void main(String[] args) {
        Env env = new Env(new HashMap<String, Object>(),null);
        LispBase.initEnv(env);
        ListParser parser = new ListParser(new StringBufferInputStream("(define w (lambda (x) ( + x x ))) (w 14)"));
        Object o  = parser.getNextObject();
        Object b  = parser.getNextObject();
        Object r = LispBase.eval(o,env);
        System.out.println(r);
        Object d = LispBase.eval(b,env);
        System.out.println(d);
    }
}
