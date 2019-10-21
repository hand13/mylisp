package com.hand13;

import java.util.HashMap;
import java.util.Map;

public class Env {
    public Map<String,Object> env;
    public Env up;
    public Env() {
        env = new HashMap<>();
    }

    public Env(Map<String, Object> env, Env up) {
        this.env = env;
        this.up = up;
    }
    public void put(String name,Object value) {
        this.env.put(name,value);
    }
    public void registerProcudure(String name,Procedure procedure) {
        this.env.put(name,procedure);
    }
    public Object getValue(String symbol){
        Object result = this.env.get(symbol);
        if(result == null && up != null) {
            result = up.getValue(symbol);
        }
        return result;
    }

    @Override
    public String toString() {
        return "environment";
    }
}
