package com.hand13;

import java.util.HashMap;
import java.util.Map;

import static com.hand13.Token.BEGIN;
import static com.hand13.Token.LAMBDA;

public class HighProcedure implements Procedure {
    private Env env;
    private List exp;
    private String[] argList;
    private int paramNumbers;

    public HighProcedure(List lambda,Env env) {
        if(lambda.fst != LAMBDA) {
            throw new RuntimeException("not a lambda");
        }
        this.env =env;
        List a = (List)LispBase.cadr(lambda);
        //lambda 体转换为begin结构
        this.exp = (List)LispBase.cddr(lambda);
        this.exp = new List(BEGIN,this.exp);
        if(a != null) {
            argList = new String[a.length()];
            List t = a;
            for(int i = 0;i<argList.length;i++) {
                argList[i] = ((Symbol)t.fst).value;
                t = (List)t.snd;
            }
            paramNumbers = argList.length;
        }else {
            paramNumbers = 0;
        }
    }


    @Override
    public boolean checkParams(List args) {
        if(paramNumbers != Procedure.INFINITE_PARAM_LENGTH) {
            return paramNumbers == args.length();
        }
        return true;
    }

    public Object apply(List args) {
        if(!checkParams(args)) {
            throw  new RuntimeException("wrong args numbers");
        }
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

    @Override
    public String toString() {
        return "lambda procedure";
    }
}
