package com.samples;

import lombok.extern.flogger.Flogger;

@Flogger
public class HelloWorld {
    public static void main(String[] args) {
        printHello();
        printHelloIndia();
    }

    private static void printHello() {
        log.atInfo().log("Hello Viral N Mehta");
    }

    private static void printHelloIndia() {
        log.atInfo().log("Hello India India");
    }
}
