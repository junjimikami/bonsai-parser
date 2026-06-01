package com.jiganaut.bonsai.grammar;

/**
 * @author Junji Mikami
 *
 */
public interface Skippable extends Rule {

    public static interface Builder extends Rule.Builder {

        public default SkipRule.Builder skip() {
            return () -> SkipRule.of(build());
        }

    }

    public default SkipRule skip() {
        return SkipRule.of(this);
    }

}
