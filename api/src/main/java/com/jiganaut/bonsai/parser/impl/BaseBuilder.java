package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;

abstract class BaseBuilder {

    private boolean isBuilt;

    void check() {
        if (isBuilt) {
            throw new IllegalStateException();
        }
    }

    void checkParameter(Object... args) {
        check();
        for (var o : args) {
            Objects.requireNonNull(o);
        }
    }

    void checkForBuild() {
        check();
        isBuilt = true;
    }

}
