package com.hand13;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class Lisp {
    private Env env = new Env();
    private boolean inited;
    public void init() {
        LispBase.initEnv(env);
        inited = true;
    }
    public void loadLibrary(String filepath){

    }
    public void doFile(String filepath)throws IOException {
        if(!inited) {
            throw new RuntimeException("not init base lib");
        }
        ListParser parser = new ListParser(new FileInputStream(filepath));
        Object o  = parser.getNextObject();
        while(o != null) {
            LispBase.eval(o, env);
            o = parser.getNextObject();
        }
    }
    public static void main(String[] args) throws Exception{
        URL filepath = Lisp.class.getResource("/main.ss");
        Lisp lisp = new Lisp();
        lisp.init();
        lisp.doFile(filepath.getPath());
    }
}
