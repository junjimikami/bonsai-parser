package com.unitedjiga.common.parsing.impl;

import java.util.Objects;

import com.unitedjiga.common.parsing.Token;

class DefaultToken implements Token {
    private final String name;
    private final String value;

    /**
     * @param name
     * @param value
     */
    DefaultToken(String name, String value) {
        Objects.requireNonNull(value);
        this.name = name;
        this.value = value;
    }
    DefaultToken(String value) {
        this(null, value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

}
