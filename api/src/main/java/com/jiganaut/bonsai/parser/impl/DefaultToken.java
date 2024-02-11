package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.parser.Token;

class DefaultToken implements Token {
    private final String value;

    /**
     * @param value
     */
    DefaultToken(String value) {
        assert value != null;
        this.value = value;
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
