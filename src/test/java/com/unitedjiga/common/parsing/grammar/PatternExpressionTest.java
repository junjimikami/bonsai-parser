package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.grammar.Expression.Kind;

class PatternExpressionTest implements ExpressionTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Nested
    class BuilderTest implements ExpressionTest.BulderTest, QuantifiableTest {
        @Override
        public PatternExpression.Builder builder() {
            return PatternExpression.builder("");
        }
    }

    @Override
    public Expression build() {
        return PatternExpression.builder("").build();
    }

    @Test
    @DisplayName("builder(st:String) [Null parameter]")
    void builderStNull() throws Exception {
        assertThrows(NullPointerException.class, () -> PatternExpression.builder((String) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("builder(pa:Pattern) [Null parameter]")
    void builderPaNull() throws Exception {
        assertThrows(NullPointerException.class, () -> PatternExpression.builder((Pattern) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("builder(st:String) [Invalid regex]")
    void builderStInvalidRegex() throws Exception {
        assertThrows(PatternSyntaxException.class, () -> PatternExpression.builder("["))
                .printStackTrace();
    }

    @Test
    @DisplayName("getKind()")
    void getKind() throws Exception {
        var pattern = build();

        assertEquals(Kind.PATTERN, pattern.getKind());
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor)")
    void acceptEv() throws Exception {
        var pattern = build();

        pattern.accept(new TestExpressionVisitor<Void, Object>() {
            @Override
            public Void visitPattern(PatternExpression expression, Object p) {
                assertEquals(pattern, expression);
                assertNull(p);
                return null;
            }
        });
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor, p:P)")
    void acceptEvP() throws Exception {
        var pattern = build();
        var arg = new Object();

        pattern.accept(new TestExpressionVisitor<Void, Object>() {
            @Override
            public Void visitPattern(PatternExpression expression, Object p) {
                assertEquals(pattern, expression);
                assertEquals(arg, p);
                return null;
            }
        }, arg);
    }

    @Test
    @DisplayName("getPattern()")
    void getPattern() throws Exception {
        var pattern = PatternExpression.builder("123ABC").build();

        assertEquals("123ABC", pattern.getPattern().pattern());
    }
}
