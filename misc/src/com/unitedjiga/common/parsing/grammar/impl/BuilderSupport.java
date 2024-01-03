package com.unitedjiga.common.parsing.grammar.impl;

import java.util.Objects;

abstract class BuilderSupport {
    private boolean isBuilt;

    protected void check() {
        if (isBuilt) {
            throw new IllegalStateException(Message.ALREADY_BUILT.format());
        }
    }

    protected void check(Object o) {
        check();
        Objects.requireNonNull(o, Message.NON_NULL_REQUIRED.format());
    }

    protected void check(String symbol, Object o) {
        check();
        Objects.requireNonNull(symbol, Message.NON_NULL_REQUIRED.format());
        Objects.requireNonNull(o, Message.NON_NULL_REQUIRED.format());
    }

    protected void checkForBuild() {
        check();
        isBuilt = true;
    }

}
