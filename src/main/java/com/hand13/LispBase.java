package com.hand13;

import static com.hand13.Token.LAMBDA;

public class LispBase {
    public static Object eval(Object value,Env env) {
        if(isExp(value)) {
            List o = evalEach((List)value,env);
            return apply((Procedure)o.fst,(List)o.snd);

        }else if(isSymbol(value)){
            return env.getValue(((Symbol)value).value);

        }else if(isPrimitive(value)) {
            return value;

        }else if(isLambda(value)) {
            return new HighProcedure((List)value,env);

        }else if(isQuoted(value)) {
            return new List("haha",null);
        }
        return null;
    }
    public static Object apply(Procedure procedure,List args) {
        return procedure.apply(args);
    }
    public static List evalEach(List value,Env env) {
        List result = null;
        if(value != null) {
            result = new List(eval(value.fst,env),evalEach((List)value.snd,env));
        }
        return result;
    }

    public static boolean isQuoted(Object value) {
        return false;
    }
    public static boolean isExp(Object value) {
        return value instanceof List && (! isInner(value));
    }

    public static boolean isInner(Object value) {
        return isLambda(value);
    }
    public static  boolean isLambda(Object value) {
        if(value instanceof List) {
            Object first = ((List) value).fst;
            return first == LAMBDA;
        }
        return false;
    }
    public static boolean isSymbol(Object value){
        return value instanceof Symbol;
    }
    public static boolean isPrimitive(Object value) {
        return value instanceof String || value instanceof Number;
    }
    public static Object car(List value) {
        return value.fst;
    }
    public static Object cdr(List value) {
        return value.snd;
    }
    public static Object cdar(List value){
        return ((List)(cdr(value))).fst;
    }
    public static Object cddr(List value) {
        return ((List)(cdr(value))).snd;
    }
    public static Object cddar(List value) {
        return ((List)cddr(value)).fst;
    }
    public static void initEnv(Env env) {
        env.put("+", new Procedure() {
            public Object apply(List args) {
                double m = 0;
                List tmp = args;
                while(tmp != null) {
                    m += ((Number)(LispBase.car(tmp))).doubleValue();
                    tmp = (List)tmp.snd;
                }
                return m;
            }
        });
        env.put("car", new Procedure() {
            public Object apply(List args) {
                return LispBase.car((List)args.fst);
            }
        });
        env.put("cons", new Procedure() {
            public Object apply(List args) {
                return new List(args.fst,((List)args.snd).fst);
            }
        });
    }
}
