package com.mcallydevelops;

import org.springframework.stereotype.Component;

@Component
public class FormatImpl implements Format {

    @Override
    public String composeGreeting(String name) {
        return "Hello " + name + "!";
    }
}