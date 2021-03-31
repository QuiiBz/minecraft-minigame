package fr.quiibz.api.logger;

public class Logger {

    /*
     *  METHODS
     */

    public static void log(String message) {

        System.out.println(message);
    }

    public static void err(String message) {

        System.err.println(message);
    }
}
