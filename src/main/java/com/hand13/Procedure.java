package com.hand13;

public interface  Procedure {
    public static int INFINITE_PARAM_LENGTH = -1;
    boolean checkParams(List args);
    Object apply(List args);
}
