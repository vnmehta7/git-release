package com.samples;

import lombok.extern.flogger.Flogger;

@Flogger
public class HelloWorld {
    public static void main(String[] args) {
        printHello();
        printHelloWorld();
    }

    private static void printHello() {
        log.atInfo().log("Hello");
    }

    private static void printHelloWorld() {
        log.atInfo().log("Hello world");
    }
}
