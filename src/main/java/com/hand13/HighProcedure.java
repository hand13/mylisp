package com.hand13;

import java.util.HashMap;
import java.util.Map;

import static com.hand13.Token.LAMBDA;

public class HighProcedure implements Procedure {
    public Env env;
    public List exp;
    public String[] argList;

    public HighProcedure(List lambda,Env env) {
        if(lambda.fst != LAMBDA) {
            throw new RuntimeException("not a lambda");
        }
        this.env =env;
        List a = (List)LispBase.cdar(lambda);
        this.exp = (List)LispBase.cddar(lambda);
        if(a != null) {
            argList = new String[a.length()];
            List t = a;
            for(int i = 0;i<argList.length;i++) {
                argList[i] = ((Symbol)t.fst).value;
                t = (List)t.snd;
            }
        }
    }
    public Object apply(List args) {
        this.putArgs(args);
        Env tmp = new Env(this.putArgs(args),env);
        return LispBase.eval(exp,tmp);
    }
    private Map<String,Object> putArgs(List args) {
        Map<String,Object> tmp = new HashMap<String, Object>();
        if(args != null) {
            if (argList.length != args.length()) {
                throw new RuntimeException("no nice arguments");
            }
            for (String s : argList) {
                if (args != null) {
                    tmp.put(s, args.fst);
                    args = (List) args.snd;
                }
            }
        }
        return tmp;
    }
}
