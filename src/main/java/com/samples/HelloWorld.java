package com.samples;

import lombok.extern.flogger.Flogger;

@Flogger
public class HelloWorld {
    public static void main(String[] args) {
        printHello();
    }

    private static void printHello() {
        log.atInfo().log("Hello world");
    }
}
