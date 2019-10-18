package com.hand13;

public abstract class PrimitiveProcedure implements Procedure{
    private Env env;
    public PrimitiveProcedure() {}
    public PrimitiveProcedure(Env env) {
        this.env = env;
    }
    @Override
    public String toString() {
        return "this is a primitiveProcedure";
    }
}
