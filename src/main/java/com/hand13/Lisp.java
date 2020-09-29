package com.hand13;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.net.URL;

import static com.hand13.SpecialValue.VOID;

public class Lisp {
    private static final Logger logger = LoggerFactory.getLogger(Lisp.class);
    private final Env baseEnv = new Env();
    private Env env;
    private boolean initialed;

    public Lisp() {
        env = new Env(baseEnv);
        env.registerProcedure("logger", new PrimitiveProcedure(2) {
            @Override
            public Object onApply(List args) {
                String level = (String) LispBase.car(args);
                String msg = (String) LispBase.cdar(args);
                if ("debug".equalsIgnoreCase(level)) {
                    logger.debug(msg);
                } else if ("info".equalsIgnoreCase(level)) {
                    logger.info(msg);
                } else if ("error".equalsIgnoreCase(level)) {
                    logger.error(msg);
                }
                return VOID;
            }
        });
    }

    public Env getEnv() {
        return env;
    }

    public void init() {
        LispBase.initEnv(baseEnv);
        //LispBase.loadLibrary(env,"lib.ss");
        initialed = true;
    }

    public void loadLibrary(String filepath) {

    }

    public void clearEnv() {
        env = new Env(baseEnv);
    }

    public void doFile(String filepath) throws IOException {
        if (!initialed) {
            throw new RuntimeException("not init base lib");
        }
        ListParser parser = new ListParser(new FileInputStream(filepath));
        Object o = parser.getNextObject();
        while (o != null) {
            LispBase.eval(o, env);
            o = parser.getNextObject();
        }
    }

    public void doString(String statement) throws IOException {
        if (!initialed) {
            throw new RuntimeException("not init base lib");
        }
        ListParser parser = new ListParser(new StringBufferInputStream(statement));
        Object o = parser.getNextObject();
        while (o != null) {
            LispBase.eval(o, env);
            o = parser.getNextObject();
        }
    }

    public static void main(String[] args) throws Exception {
        URL filepath = Lisp.class.getResource("/main.ss");
        Lisp lisp = new Lisp();
        lisp.init();
        lisp.doString("(display \"hello world\")");
        lisp.doString("(display \"hello world\")");
        lisp.doString("(display \"hello world\")");
        lisp.doString("(logger \"info\" \"die\")");
        lisp.doFile(filepath.getPath());
    }
}
