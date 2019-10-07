package com.example.hw10.exception;

public class TaskNotExistException extends Exception {
    @Override
    public String getMessage() {
        return "this task not found !!!";
    }
}
