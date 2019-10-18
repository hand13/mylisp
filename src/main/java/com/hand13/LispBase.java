package com.hand13;

import static com.hand13.Token.DEFINE;
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
            return ((QuotedObject)value).value;
        }else if(isDefine(value)) {
            define(value,env);
        }
        return null;
    }

    public static void define(Object value,Env env) {
        List def = (List)value;
        Symbol varSymbol = (Symbol)cdar(def);
        Object exp = (car((List)(cddr(def))));
        String var = varSymbol.value;
        if(env.getValue(var) != null) {
            throw new RuntimeException("re definition");
        }
        env.put(var,eval(exp,env));
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
    public static boolean isDefine(Object value) {
        if(value instanceof List) {
            Object first = ((List) value).fst;
            return first == DEFINE;
        }
        return false;
    }

    public static boolean isQuoted(Object value) {
        return value instanceof QuotedObject;
    }
    public static boolean isExp(Object value) {
        return value instanceof List && (! isInner(value));
    }

    public static boolean isInner(Object value) {
        return isLambda(value) || isDefine(value);
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
    public static void initEnv(final Env env) {
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
        env.put("car", new PrimitiveProcedure() {
            public Object apply(List args) {
                return LispBase.car((List)args.fst);
            }
        });
        env.put("cdr", new PrimitiveProcedure() {
            public Object apply(List args) {
                return LispBase.cdr((List)args.fst);
            }
        });
        env.put("cons", new PrimitiveProcedure() {
            public Object apply(List args) {
                return new List(args.fst,((List)args.snd).fst);
            }
        });
        env.put("display", new PrimitiveProcedure() {
            @Override
            public Object apply(List args) {
                if(args.fst != null) {
                    System.out.println(args.fst);
                }
                return null;
            }
        });
        env.put("eval", new PrimitiveProcedure() {
            @Override
            public Object apply(List args) {
                return eval((car(args)),env);
            }
        });
        env.put("null?", new PrimitiveProcedure() {
            @Override
            public Object apply(List args) {
                return args == null || (args.fst == null && args.snd == null);
            }
        });
    }
}
