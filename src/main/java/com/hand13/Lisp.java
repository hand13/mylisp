package com.hand13;

import java.io.StringBufferInputStream;
import java.util.HashMap;

public class Lisp {
    public static void main(String[] args) {
        Env env = new Env(new HashMap<String, Object>(),null);
        LispBase.initEnv(env);
        ListParser parser = new ListParser(
                new StringBufferInputStream("(begin (display \"hello\") (display \"world\n\") ( if ( > 1 2) (display \"hand13\") (display \"greet\") ))"));
        Object o  = parser.getNextObject();
        LispBase.eval(o, env);
    }
}
