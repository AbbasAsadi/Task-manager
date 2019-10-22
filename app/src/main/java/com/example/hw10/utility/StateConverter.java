package com.example.hw10.utility;


import com.example.hw10.model.State;

import org.greenrobot.greendao.converter.PropertyConverter;

public class StateConverter implements PropertyConverter<State, Integer> {
    @Override
    public State convertToEntityProperty(Integer databaseValue) {
        for (State state : State.values()){
            if (state.getI() == databaseValue)
                return state;
        }
        return null;
    }

    @Override
    public Integer convertToDatabaseValue(State entityProperty) {
        return entityProperty.getI();
    }
}
