package com.jiganaut.bonsai.impl;

import java.util.Objects;

/**
 * 
 * @author Junji Mikami
 */
public abstract class BaseBuilder {
    private boolean isBuilt;

    protected void check() {
        if (isBuilt) {
            throw new IllegalStateException(Message.ALREADY_BUILT.format());
        }
    }

    protected void checkParameter(Object... args) {
        for (var o : args) {
            Objects.requireNonNull(o, Message.NULL_PARAMETER.format());
        }
        check();
    }

    protected void checkForBuild() {
        check();
        isBuilt = true;
    }

}
