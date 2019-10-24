package com.example.hw10.model;

import androidx.annotation.NonNull;

public enum State {
    TODO(0),
    DOING(1),
    DONE(2);

    private int i;

    State(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case DONE:
                return "DONE";
            case DOING:
                return "DOING";
            case TODO:
                return "TODO";
        }
        return null;
    }
}

