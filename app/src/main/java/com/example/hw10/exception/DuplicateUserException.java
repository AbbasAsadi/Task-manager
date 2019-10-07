package com.example.hw10.exception;

public class DuplicateUserException extends Exception {
    @Override
    public String getMessage() {
        return "Duplicate user!!! , this userName is exist , choose another ._. ";
    }
}
