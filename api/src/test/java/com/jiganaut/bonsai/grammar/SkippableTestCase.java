package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

interface SkippableTestCase extends TestCase {
    Skippable createTarget();

    default Skippable createTargetSkippable() {
        return createTarget();
    }

    @Test
    @DisplayName("skip()")
    default void skip() throws Exception {
        var target = createTargetSkippable();
        var skip = target.skip();

        assertEquals(target, skip.getRule());
    }

}
