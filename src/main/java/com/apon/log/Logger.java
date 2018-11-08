package com.apon.log;

public class Logger {
    public static void logError(String message) {
        System.out.println(message);
    }

    public static void logError(Exception e) {
        e.printStackTrace();
    }

    public static void logInfo(String message) {
        System.out.println(message);
    }
}
