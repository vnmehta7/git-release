package com.samples;

import lombok.extern.flogger.Flogger;

@Flogger
public class HelloWorld {
    public static void main(String[] args) {
        log.atInfo().log("Hello world");
    }
}
