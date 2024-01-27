package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.unitedjiga.common.parsing.grammar.Expression.Kind;

class PatternExpressionTest implements ExpressionTest {

    @Nested
    class BuilderTest implements ExpressionTest.BulderTest, QuantifiableTest {

        @Override
        public PatternExpression.Builder builder() {
            return PatternExpression.builder("");
        }

        @Nested
        class QuantifierBuilderTest implements QuantifierExpressionTest.BuilderTest {
            @Override
            public QuantifierExpression.Builder builder() {
                return PatternExpressionTest.BuilderTest.this.builder().opt();
            }
        }

        @Nested
        class QuantifierTest implements QuantifierExpressionTest {
            @Override
            public Quantifiable builder() {
                return PatternExpressionTest.BuilderTest.this.builder();
            }
        }
    }

    @Override
    public PatternExpression build() {
        return PatternExpression.builder("").build(Stubs.DUMMY_PRODUCTION_SET);
    }

    @Override
    public Kind kind() {
        return Kind.PATTERN;
    }

    @Override
    public ExpressionVisitor<Object[], String> elementVisitor() {
        return new TestExpressionVisitor<Object[], String>() {
            @Override
            public Object[] visitPattern(PatternExpression pattern, String p) {
                return new Object[] { pattern, p };
            }
        };
    }

    @Test
    @DisplayName("builder(st:String) [Null parameter]")
    void builderStInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> PatternExpression.builder((String) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("builder(pa:Pattern) [Null parameter]")
    void builderPaInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> PatternExpression.builder((Pattern) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("builder(st:String) [Invalid regex]")
    void builderStInCaseInvalidRegex() throws Exception {
        assertThrows(PatternSyntaxException.class, () -> PatternExpression.builder("["))
                .printStackTrace();
    }

    @Test
    @DisplayName("builder(st:String)")
    void builderSt() throws Exception {
        PatternExpression.builder("");
    }

    @Test
    @DisplayName("builder(pa:Pattern)")
    void builderPa() throws Exception {
        PatternExpression.builder(Pattern.compile(""));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = { "test", "[0-9]" })
    @DisplayName("getPattern()")
    void getPattern(String regex) throws Exception {
        var pattern = PatternExpression.builder(regex).build(Stubs.DUMMY_PRODUCTION_SET);

        assertEquals(regex, pattern.getPattern().pattern());
    }
}
