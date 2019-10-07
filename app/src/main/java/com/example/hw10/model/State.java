package com.example.hw10.model;

public enum State {
    TODO(0) ,
    DOING(1) ,
    DONE(2);

    private int i;

    State(int i){this.i = i;}

    public int getI() {
        return i;
    }
}

