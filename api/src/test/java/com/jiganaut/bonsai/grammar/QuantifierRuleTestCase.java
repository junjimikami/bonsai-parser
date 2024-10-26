package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.OptionalInt;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

interface QuantifierRuleTestCase extends RuleTestCase {

    @Override
    QuantifierRule createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.QUANTIFIER;
    }

    int expectedMinCount();

    OptionalInt expectedMaxCount();

    Rule expectedRule();

    @Test
    @DisplayName("getMinCount()")
    default void getMinCount() throws Exception {
        var target = createTarget();

        assertEquals(expectedMinCount(), target.getMinCount());
    }

    @Test
    @DisplayName("getMaxCount()")
    default void getMaxCount() throws Exception {
        var target = createTarget();

        assertEquals(expectedMaxCount().isEmpty(), target.getMaxCount().isEmpty());
        if (expectedMaxCount().isPresent()) {
            assertEquals(expectedMaxCount().getAsInt(), target.getMaxCount().getAsInt());
        }
    }

    @Test
    @DisplayName("getRule()")
    default void getRule() throws Exception {
        var target = createTarget();

        assertEquals(expectedRule(), target.getRule());
    }

    @Test
    @DisplayName("stream()")
    default void stream() throws Exception {
        var target = createTarget();

        var expected = Stream.generate(this::expectedRule)
                .limit(expectedMaxCount().orElse(9))
                .limit(9)
                .toList();
        var actual = target.stream()
                .limit(9)
                .toList();
        assertEquals(expected, actual);
    }

}
