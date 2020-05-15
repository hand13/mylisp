package com.hand13;

import javax.management.relation.RoleUnresolved;

public abstract class PrimitiveProcedure implements Procedure{
    private Env env;
    private int paramNumbers;

    public PrimitiveProcedure() {
        paramNumbers = 0;
    }
    public PrimitiveProcedure(int paramNumbers) {
        this.paramNumbers = paramNumbers;
    }
    public PrimitiveProcedure(Env env) {
        this.env = env;
        paramNumbers = 0;
    }
    public PrimitiveProcedure(Env env,int paramNumbers) {
        this.env = env;
        this.paramNumbers = paramNumbers;
    }

    //只检查数量
    @Override
    public boolean checkParams(List args) {
        if(paramNumbers != Procedure.INFINITE_PARAM_LENGTH) {
            return paramNumbers == args.length();
        }
        return true;
    }

    @Override
    public Object apply(List args) {
        if(!checkParams(args)) {
            throw new RuntimeException("wrong args numbers");
        }
        return this.onApply(args);
    }
    public abstract Object onApply(List args);

    @Override
    public String toString() {
        return "this is a primitiveProcedure";
    }
}
