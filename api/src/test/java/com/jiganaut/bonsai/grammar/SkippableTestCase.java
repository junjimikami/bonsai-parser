package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.TestCase;

/**
 * 
 * @author Junji Mikami
 */
interface SkippableTestCase extends TestCase {
    Skippable createTarget();

    @Test
    @DisplayName("skip()")
    default void skip() throws Exception {
        var target = createTarget();
        var skip = target.skip();

        assertEquals(Rule.Kind.SKIP, skip.getKind());
        assertEquals(target, skip.getRule());
    }

}
