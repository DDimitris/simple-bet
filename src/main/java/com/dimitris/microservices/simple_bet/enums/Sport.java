package com.dimitris.microservices.simple_bet.enums;

import lombok.Getter;

@Getter
public enum Sport {
    FOOTBALL(1),
    BASKETBALL(2);

    private final int code;

    Sport(int code) {
        this.code = code;
    }

    public static Sport toName(int code) {
        return switch (code) {
            case 1 -> FOOTBALL;
            case 2 -> BASKETBALL;
            default -> throw new IllegalArgumentException("Unknown code: " + code);
        };
    }

    public static int toCode(String name) {
        return switch (name.toUpperCase()) {
            case "FOOTBALL" -> FOOTBALL.code;
            case "BASKETBALL" -> BASKETBALL.code;
            default -> throw new IllegalArgumentException("Unknown sport name: " + name);
        };
    }

    public static String nameOf(int code) {
        return toName(code).name().toUpperCase();
    }

}
