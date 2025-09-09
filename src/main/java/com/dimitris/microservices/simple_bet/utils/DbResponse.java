package com.dimitris.microservices.simple_bet.utils;

import lombok.Getter;

@Getter
public class DbResponse<T> {
    private final T data;
    private final String error;

    public DbResponse(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public static <T> DbResponse<T> success(T data) {
        return new DbResponse<>(data, null);
    }

    public static <T> DbResponse<T> failure(String error) {
        return new DbResponse<>(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }
}
