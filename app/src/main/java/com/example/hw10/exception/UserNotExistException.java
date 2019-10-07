package com.example.hw10.exception;

public class UserNotExistException extends Exception {
    @Override
    public String getMessage() {
        return "this user not found !!!";
    }
}
