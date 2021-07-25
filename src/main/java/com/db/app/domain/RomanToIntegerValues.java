package com.db.app.domain;

/**
 * Representation of Roman values with their respective integral values
 */
//TODO - Not required
public enum RomanToIntegerValues {

    I(1) ,
    V(5),
    X(10),
    L(50),
    C(100),
    D(500),
    M(1000);

    private int value;

    RomanToIntegerValues(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return this.value;
    }
}
