package com.hand13;

import java.util.ArrayList;

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
    public static java.util.List<Object>  toList(List list) {
        ArrayList<Object> result = new ArrayList<>();
        for(Object o :list) {
            result.add(o);
        }
        return result;
    }
}
