package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.parser.Token;

class DefaultToken implements Token {
    private final String name;
    private final String value;

    /**
     * @param value
     */
    DefaultToken(String name, String value) {
        assert value != null;
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
