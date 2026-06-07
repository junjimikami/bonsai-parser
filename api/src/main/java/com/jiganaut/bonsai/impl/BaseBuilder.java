package com.jiganaut.bonsai.impl;

/**
 *
 * @author Junji Mikami
 */
public abstract class BaseBuilder {
    private boolean isBuilt;

    protected void check() {
        if (isBuilt) {
            throw new IllegalStateException(Message.STATE_ALREADY_COMPLETED.format("build"));
        }
    }

    protected void checkForBuild() {
        check();
        isBuilt = true;
    }

}
