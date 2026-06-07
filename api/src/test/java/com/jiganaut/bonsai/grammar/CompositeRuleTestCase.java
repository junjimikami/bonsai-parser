package com.jiganaut.bonsai.grammar;

import org.junit.jupiter.api.Nested;

/**
 *
 * @author Junji Mikami
 */
interface CompositeRuleTestCase<T> extends QuantifiableTestCase<T> {

    @Nested
    interface BuilderTestCase<T> extends QuantifiableTestCase.BuilderTestCase<T> {

    }

}
