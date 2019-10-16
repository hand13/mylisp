package com.hand13;

import java.util.HashMap;

public class Lisp {
    public static void main(String[] args) {
        Env env = new Env(new HashMap<String, Object>(),null);
        LispBase.initEnv(env);
        List a2 = new List(new Symbol("cons"),new List(18,new List(17,null)));
        List a1 = new List(new Symbol("car"),new List(a2,null));
        List test = new List(new Symbol("+"),new List(16,new List(15,new List(a1,null))));
        System.out.println(LispBase.eval(test,env));
    }
}
