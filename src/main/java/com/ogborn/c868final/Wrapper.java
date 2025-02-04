package com.ogborn.c868final;

/**
 * A simple wrapper class to allow building an executable JAR with Maven and JavaFX.
 * This class serves as the entry point, delegating execution to the {@link Main} class.
 */
public class Wrapper {
    /**
     * The main method serves as the entry point for the application.
     * It delegates execution to the {@link Main#main(String[])} method.
     *
     * @param args command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        Main.main(args);
    }
}
