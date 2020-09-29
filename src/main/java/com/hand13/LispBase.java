package com.hand13;

import com.hand13.exception.LispException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import static com.hand13.Token.*;

//lisp 求值中不得存在空指针
public class LispBase {
    public static Object eval(Object value, Env env) {
        if (isExp(value)) {
            List o = evalEach((List) value, env);
            return apply((Procedure) o.fst, (List) o.snd);

        } else if (isSymbol(value)) {
            Object result = env.getValue(((Symbol) value).value);
            if (result == null) {
                throw new LispException("no " + value + " symbol");
            }
            return result;
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
        return SpecialValue.VOID;
    }

    public static Object IF(Object value, Env env) {
        List exp = (List) value;
        Object predicate = cdar(exp);
        Object yes = cddar(exp);
        Object no = (cddar((List) cdr(exp)));
        Boolean p = (Boolean) (eval(predicate, env));
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
        while (!LispUtils.isNull(tmp)) {
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
        env.put(var, eval(exp, env));
    }

    public static Object apply(Procedure procedure, List args) {
        return procedure.apply(args);
    }

    public static List evalEach(List value, Env env) {
        List result = List.NULL_VALUE;
        if (!LispUtils.isNull(value)) {
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
        return value instanceof String
                || value instanceof Number
                || value instanceof Character
                || value instanceof Boolean;
    }

    public static Object car(Object value) {
        if (LispUtils.isNull(value) || !(value instanceof List)) {
            throw new RuntimeException("not a pair");
        }
        return ((List) value).fst;
    }

    public static Object cdr(Object value) {
        if (LispUtils.isNull(value) || !(value instanceof List)) {
            throw new RuntimeException("not a pair");
        }
        return ((List) value).snd;
    }

    public static Object cdar(List value) {
        return car(cdr(value));
    }

    public static Object cddr(List value) {
        return cdr(cdr(value));
    }

    public static Object cddar(List value) {
        return car(cdr(cdr(value)));
    }

    public static void initEnv(final Env env) {
        env.put("+", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            public Object onApply(List args) {
                BigDecimal m = BigDecimal.ZERO;
                for (Object o : args) {
                    m = m.add((BigDecimal) o);
                }
                return m;
            }

        });

        env.put("-", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                BigDecimal m = (BigDecimal) car(args);
                List tmp = (List) cdr(args);
                for (Object o : tmp) {
                    m = m.subtract((BigDecimal) o);
                }
                return m;
            }
        });


        env.put("*", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                BigDecimal m = BigDecimal.ONE;
                for (Object o : args) {
                    m = m.multiply((BigDecimal) o);
                }
                return m;
            }
        });
        env.put("/", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                BigDecimal m = (BigDecimal) car(args);
                List tmp = (List) cdr(args);
                for (Object o : tmp) {
                    m = m.divide((BigDecimal) o);
                }
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
                return car(car(args));
            }
        });
        env.put("cdr", new PrimitiveProcedure(1) {
            public Object onApply(List args) {
                return cdr(args.fst);
            }
        });
        env.put("cons", new PrimitiveProcedure(2) {
            public Object onApply(List args) {
                return new List(car(args), cdar(args));
            }
        });
        env.put("display", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                System.out.print(show(car(args)));
                return SpecialValue.VOID;
            }
        });
        env.put("show", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                return show(car(args));
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
                return LispUtils.isNull(car(args));
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

        env.put("append", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                StringBuilder sb = new StringBuilder();
                for (Object o : args) {
                    if (!(o instanceof String)) {
                        throw new RuntimeException("not a string");
                    }
                    sb.append(o);
                }
                return sb.toString();
            }
        });

        env.put("number->string", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                Object o = car(args);
                if (!(o instanceof BigDecimal)) {
                    throw new RuntimeException("not a number");
                }
                return o.toString();
            }
        });

        env.put("string->number", new PrimitiveProcedure(1) {
            @Override
            public Object onApply(List args) {
                Object o = car(args);
                if (!(o instanceof String)) {
                    throw new RuntimeException("not a String");
                }
                return new BigDecimal(o.toString());
            }
        });

        env.put("new-object", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                String className = (String) car(args);
                java.util.List<Object> params = LispUtils.toList((List) cdr(args));
                try {
                    Class<?> clazz = Class.forName(className);
                    Class<?>[] sig = new Class[params.size()];
                    for (int i = 0; i < params.size(); i++) {
                        sig[i] = params.get(i).getClass();
                    }
                    Constructor<?> constructor = clazz.getConstructor(sig);
                    return constructor.newInstance(params.toArray());
                } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        env.put("static-method-call", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                String method = (String) car(args);
                String className = method.substring(0, method.lastIndexOf("."));
                String methodName = method.substring(method.lastIndexOf(".") + 1);
                java.util.List<Object> params = LispUtils.toList((List) cdr(args));
                try {
                    Class<?> clazz = Class.forName(className);
                    Class<?>[] sig = new Class[params.size()];
                    for (int i = 0; i < params.size(); i++) {
                        sig[i] = params.get(i).getClass();
                    }
                    Method md = clazz.getMethod(methodName, sig);
                    Object result = md.invoke(null, params);
                    if (result == null) {
                        return SpecialValue.VOID;
                    }
                    return result;
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        env.put("object-method-call", new PrimitiveProcedure(Procedure.INFINITE_PARAM_LENGTH) {
            @Override
            public Object onApply(List args) {
                String methodName = (String) car(args);
                Object target = cdar(args);
                return null;
            }
        });

    }

    public static boolean loadLibrary(Env env, String path) {
        return true;
    }

    public static String show(Object o) {
        return o.toString();
    }
}
