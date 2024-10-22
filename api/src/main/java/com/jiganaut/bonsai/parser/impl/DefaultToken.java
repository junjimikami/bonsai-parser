package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.Token;

class DefaultToken extends AbstractTree implements Token {

    static class Builder extends AbstractTree.Builder implements TerminalNode.Builder {
        @Override
        public DefaultToken.Builder setName(String name) {
            super.setName(name);
            return this;
        }

        @Override
        public DefaultToken.Builder setValue(String value) {
            checkParameter(value);
            super.setValue(value);
            return this;
        }

        @Override
        public DefaultToken build() {
            checkParameter(value);
            checkForBuild();
            return new DefaultToken(name, value);
        }
    }

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
