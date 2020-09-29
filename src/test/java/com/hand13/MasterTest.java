package com.hand13;

import org.junit.Test;

import java.net.URL;

public class MasterTest {
    @Test
    public void mainTest() throws Exception {
        URL filepath = Lisp.class.getResource("/main.ss");
        Lisp lisp = new Lisp();
        lisp.init();
        lisp.doFile(filepath.getPath());
    }
}
