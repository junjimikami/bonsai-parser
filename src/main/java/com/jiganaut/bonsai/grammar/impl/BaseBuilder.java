package com.jiganaut.bonsai.grammar.impl;

import java.util.Objects;

abstract class BaseBuilder {
    private boolean isBuilt;

    protected void check() {
        if (isBuilt) {
            throw new IllegalStateException(Message.ALREADY_BUILT.format());
        }
    }

    protected void checkParameter(Object o) {
        check();
        Objects.requireNonNull(o, Message.NULL_PARAMETER.format());
    }

    protected void checkForBuild() {
        check();
        isBuilt = true;
    }

}
