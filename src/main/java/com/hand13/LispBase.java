package com.hand13;

import java.math.BigDecimal;

import static com.hand13.Token.*;

public class LispBase {
    public static Object eval(Object value, Env env) {
        if (isExp(value)) {
            List o = evalEach((List) value, env);
            return apply((Procedure) o.fst, (List) o.snd);

        } else if (isSymbol(value)) {
            return env.getValue(((Symbol) value).value);

        } else if (isPrimitive(value)) {
            return value;

        } else if (isLambda(value)) {
            return new HighProcedure((List) value, env);

        } else if (isQuoted(value)) {

            return ((QuotedObject) value).value;

        } else if (isDefine(value)) {

            define(value, env);

        } else if (isIf(value)) {

            return IF(value, env);

        } else if (isBegin(value)) {

            return begin(value, env);
        } else if (isLet(value)) {
            return let(value, env);
        }
        return null;
    }

    public static Object IF(Object value, Env env) {
        List exp = (List) value;
        List predicate = (List) cdar(exp);
        Object yes = cddar(exp);
        Object no = (cddar((List) cdr(exp)));
        Boolean p = (Boolean) (eval(predicate, env));
        if (p == null) {
            throw new RuntimeException("if null occurs");
        }
        if (p) {
            return eval(yes, env);
        } else {

            return eval(no, env);
        }
    }

    public static Object let(Object value, Env env) {
        List stat = (List) value;
        List varLet = (List) cdar(stat);
        List expr = (List) cddr(stat);
        //转换为lambda + param求值
        return null;
    }

    public static Object begin(Object value, Env env) {
        List exps = (List) (cdr((List) value));
        if (exps == null) {
            throw new RuntimeException("begin is null");
        }
        List tmp = exps;
        Object result = null;
        while (tmp != null) {
            result = eval(car(tmp), env);
            tmp = (List) cdr(tmp);
        }
        return result;
    }

    public static void define(Object value, Env env) {
        List def = (List) value;
        Symbol varSymbol = (Symbol) cdar(def);
        Object exp = (car((List) (cddr(def))));
        String var = varSymbol.value;
        /*
        if(env.getValue(var) != null) {
            throw new RuntimeException("re definition");
        }*/
        env.put(var, eval(exp, env));
    }

    public static Object apply(Procedure procedure, List args) {
        return procedure.apply(args);
    }

    public static List evalEach(List value, Env env) {
        List result = null;
        if (value != null) {
            result = new List(eval(value.fst, env), evalEach((List) value.snd, env));
        }
        return result;
    }

    public static boolean isDefine(Object value) {
        if (value instanceof List) {
            Object first = ((List) value).fst;
            return first == DEFINE;
        }
        return false;
    }

    public static boolean isQuoted(Object value) {
        return value instanceof QuotedObject;
    }

    public static boolean isExp(Object value) {
        return value instanceof List && (!isInner(value));
    }

    public static boolean isInner(Object value) {
        return isLambda(value) || isDefine(value) || isBegin(value) || isIf(value) || isLet(value);
    }

    public static boolean fstToken(Object value, Token token) {
        if (value instanceof List) {
            Object first = ((List) value).fst;
            return first == token;
        }
        return false;

    }

    public static boolean isLambda(Object value) {
        return fstToken(value, LAMBDA);
    }

    public static boolean isIf(Object value) {
        return fstToken(value, IF);
    }

    public static boolean isLet(Object value) {
        return fstToken(value, LET);
    }

    public static boolean isBegin(Object value) {
        return fstToken(value, BEGIN);
    }

    public static boolean isSymbol(Object value) {
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

    public static Object cdar(List value) {
        return ((List) (cdr(value))).fst;
    }

    public static Object cddr(List value) {
        return ((List) (cdr(value))).snd;
    }

    public static Object cddar(List value) {
        return ((List) cddr(value)).fst;
    }

    public static void initEnv(final Env env) {
        env.put("+", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            public Object onApply(List args) {
                BigDecimal m = BigDecimal.ZERO;
                List tmp = args;
                while (tmp != null) {
                    m = m.add((BigDecimal) car(tmp));
                    tmp = (List) tmp.snd;
                }
                return m;
            }

        });

        env.put("-", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                BigDecimal m = (BigDecimal) car(args);
                List tmp = (List) cdr(args);
                while (tmp != null) {
                    m = m.subtract((BigDecimal) car(tmp));
                    tmp = (List) tmp.snd;
                }
                return m;
            }
        });


        env.put("*", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                BigDecimal m = (BigDecimal) car(args);
                List tmp = (List) cdr(args);
                m = m.multiply((BigDecimal) car(tmp));
                return m;
            }
        });
        env.put("/", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                BigDecimal m = (BigDecimal) car(args);
                List tmp = (List) cdr(args);
                m = m.divide((BigDecimal) car(tmp), 10, BigDecimal.ROUND_HALF_UP);
                return m;
            }
        });

        env.put("%", new PrimitiveProcedure(2) {
            @Override
            public Object onApply(List args) {
                BigDecimal m = (BigDecimal) car(args);
                List tmp = (List) cdr(args);
                m = m.remainder((BigDecimal) car(tmp));
                return m;
            }
        });

        env.put("car", new PrimitiveProcedure(1) {
            public Object onApply(List args) {
                return car((List) args.fst);
            }
        });
        env.put("cdr", new PrimitiveProcedure(1) {
            public Object onApply(List args) {
                return cdr((List) args.fst);
            }
        });
        env.put("cons", new PrimitiveProcedure(2) {
            public Object onApply(List args) {
                return new List(args.fst, ((List) args.snd).fst);
            }
        });
        env.put("display", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                System.out.print(show(args.fst));
                return null;
            }
        });
        env.put("show", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                return show(args.fst);
            }
        });
        env.put("eval", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                return eval((car(args)), env);
            }
        });
        env.put("null?", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                return args == null || car(args) == null;
            }
        });
        env.put(">", new PrimitiveProcedure(2) {
            @Override
            public Object onApply(List args) {
                BigDecimal a1 = (BigDecimal) car(args);
                BigDecimal a2 = (BigDecimal) cdar(args);
                return a1.compareTo(a2) > 0;
            }
        });
        env.put("<", new PrimitiveProcedure(2) {
            @Override
            public Object onApply(List args) {
                BigDecimal a1 = (BigDecimal) car(args);
                BigDecimal a2 = (BigDecimal) cdar(args);
                return a1.compareTo(a2) < 0;
            }
        });
        env.put("=", new PrimitiveProcedure(2) {
            @Override
            public Object onApply(List args) {
                BigDecimal a1 = (BigDecimal) car(args);
                BigDecimal a2 = (BigDecimal) cdar(args);
                return a1.equals(a2);
            }
        });
        env.put("and", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                Boolean result = true;
                for (Object o : args) {
                    if (!(Boolean) o) {
                        result = false;
                        break;
                    }
                }
                return result;
            }
        });
        env.put("or", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                Boolean result = false;
                for (Object o : args) {
                    if ((Boolean) o) {
                        result = true;
                        break;
                    }
                }
                return result;
            }
        });
        env.put("not", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                return !(Boolean) (car(args));
            }
        });
        env.put("nullcar?", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                List m = (List) car(args);
                return m == null || m.fst == null;
            }
        });
    }

    public static boolean loadLibrary(Env env, String path) {
        return true;
    }

    public static String show(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return "()";
        }
    }
}
