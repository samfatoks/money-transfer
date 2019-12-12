package com.demo.moneytransfer.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorMapper {
    private static Map<Integer,Integer> errorMap = new HashMap<>();
    static {
        errorMap.put(-1, 500);
        errorMap.put(3, 400);
        errorMap.put(4, 404);
        errorMap.put(5, 400);
        errorMap.put(6, 400);
        errorMap.put(7, 400);
    }
    public static Integer getStatusCode(int errorCode) {
        return errorMap.getOrDefault(errorCode, 500);
    }
}
