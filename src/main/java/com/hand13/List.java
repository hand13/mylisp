package com.hand13;

import java.util.Iterator;

public class List implements Iterable<Object>{

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
            a = ((List) a).snd;
        }
        if(a != null) {
            throw new RuntimeException("this is not a p list");
        }
        return i;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("( ");
        for(Object o : this) {
            str.append(LispBase.show(o)).append(" ");
        }
        str.append(")");
        return str.toString();
    }

    @Override
    public Iterator<Object> iterator() {
        if(!LispUtils.isPList(this)) {
            throw  new RuntimeException("not a plist");
        }
        return new ListIterator();
    }
    class  ListIterator implements Iterator<Object> {

        private List currentNode;
        ListIterator() {
            currentNode = List.this;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public Object next() {
            Object r = currentNode.fst;
            currentNode = (List)currentNode.snd;
            return r;
        }

        @Override
        public void remove() {
            throw new RuntimeException("not support");
        }
    }
}
