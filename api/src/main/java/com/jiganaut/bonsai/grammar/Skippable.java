package com.jiganaut.bonsai.grammar;

/**
 * @author Junji Mikami
 *
 */
public interface Skippable<T> extends Rule<T> {

    public static interface Builder<T> extends Rule.Builder<T> {

        public default SkipRule.Builder<T> skip() {
            return () -> SkipRule.of(build());
        }

    }

    public default SkipRule<T> skip() {
        return SkipRule.of(this);
    }

}
