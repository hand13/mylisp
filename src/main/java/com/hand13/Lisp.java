package com.hand13;

import java.util.HashMap;

public class Lisp {
    public static void main(String[] args) {
        Env env = new Env(new HashMap<String, Object>(),null);
        LispBase.initEnv(env);
    }
}
