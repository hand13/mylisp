package com.hand13;

public class List {

    public Object fst;
    public Object snd;
    public List(Object fst,Object snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public int length() {
        Object a = this;
        int i = 0;
        while(a instanceof List) {
            i++;
            a = this.snd;
        }
        if(a != null) {
            throw new RuntimeException("this is not a p list");
        }
        return i;
    }
}
