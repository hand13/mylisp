package com.hand13;

public class QuotedObject {
    public Object value;
    public QuotedObject(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "quoted " + value.toString();
    }
}
