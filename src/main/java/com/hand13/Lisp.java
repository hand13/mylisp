package com.hand13;

import java.io.StringBufferInputStream;
import java.util.HashMap;

public class Lisp {
    public static void main(String[] args) {
        Env env = new Env(new HashMap<String, Object>(),null);
        LispBase.initEnv(env);
        ListParser parser = new ListParser(new StringBufferInputStream("(define a 12) (eval 'a)"));
        Object o  = parser.getNextObject();
        Object b  = parser.getNextObject();
        System.out.println(LispBase.eval(o, env));
        System.out.println(LispBase.eval(b, env));
        System.out.println("end");
    }
}
