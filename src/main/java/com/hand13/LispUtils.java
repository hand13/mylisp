package com.hand13;

public class LispUtils {
    public static Boolean isNull(Object list) {
        return list instanceof List && ((List) list).fst == null && ((List) list).snd == null;
    }
    public static Boolean isList(List list) {
        if(isNull(list)) {
            return true;
        }
        if(list.snd instanceof List) {
            return isList((List) list.snd);
        }
        return false;
    }
}
