package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author Junji Mikami
 */
class TokenTest {

    @Test
    @DisplayName("of(name:String, value:String) [value == null]")
    void ofWhenValueIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> Token.of("", null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("ofUnnamed(value:String) [value == null]")
    void ofUnnamedWhenValueIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> Token.ofUnnamed(null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @ParameterizedTest
    @CsvSource(nullValues = "nil", value = {
            "nil, ''",
            " '', ''",
            "  1, 2",
            "  a, b",
            "  [, *",
    })
    @DisplayName("of(name:String, value:String)")
    void of(String name, String value) throws Exception {
        var tree = Token.of(name, value);

        assertEquals(Tree.Kind.TERMINAL, tree.getKind());
        assertEquals(name, tree.getName());
        assertEquals(value, tree.getValue());
        assertTrue(tree.subTrees().findAny().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "a", "[", "*"})
    @DisplayName("ofUnnamed(value:String)")
    void ofUnnamed(String value) throws Exception {
        var tree = Token.ofUnnamed(value);

        assertEquals(Tree.Kind.TERMINAL, tree.getKind());
        assertEquals(null, tree.getName());
        assertEquals(value, tree.getValue());
        assertTrue(tree.subTrees().findAny().isEmpty());
    }

    @Nested
    class TestCase1 implements TokenTestCase<String> {

        @Override
        public Token<String> createTarget() {
            return Token.of(expectedName(), expectedValue());
        }

        @Override
        public String expectedName() {
            return "NAME";
        }

        @Override
        public String expectedValue() {
            return "VALUE";
        }

    }

}

