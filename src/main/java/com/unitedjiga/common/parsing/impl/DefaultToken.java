package com.unitedjiga.common.parsing.impl;

import java.util.Objects;

import com.unitedjiga.common.parsing.Token;

class DefaultToken implements Token {
    private final String value;

    /**
     * @param value
     */
    DefaultToken(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
