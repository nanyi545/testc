package com.ww.performancechore.rv.rv;

public class Logger {




    public static String currentLocation() {
        StackTraceElement classMethod = new Throwable().getStackTrace()[2];
        String   currMethod = classMethod.getMethodName();
        String   fullClass  = classMethod.getClassName();
        String[] smplClass  = fullClass.split("\\.");
        return smplClass[smplClass.length - 1] + "." + currMethod;
    }



}
