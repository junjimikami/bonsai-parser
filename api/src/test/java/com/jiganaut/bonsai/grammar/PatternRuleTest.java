package com.jiganaut.bonsai.grammar;

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

import com.jiganaut.bonsai.grammar.Rule.Kind;

class PatternRuleTest implements RuleTest {

    @Nested
    class BuilderTest implements RuleTest.BulderTest, QuantifiableTest {

        @Override
        public PatternRule.Builder builder() {
            return PatternRule.builder("");
        }

        @Nested
        class QuantifierBuilderTest implements QuantifierRuleTest.BuilderTest {
            @Override
            public QuantifierRule.Builder builder() {
                return PatternRuleTest.BuilderTest.this.builder().opt();
            }
        }

        @Nested
        class QuantifierTest implements QuantifierRuleTest {
            @Override
            public Quantifiable builder() {
                return PatternRuleTest.BuilderTest.this.builder();
            }
        }
    }

    @Override
    public PatternRule build() {
        return PatternRule.builder("").build(Stubs.DUMMY_PRODUCTION_SET);
    }

    @Override
    public Kind kind() {
        return Kind.PATTERN;
    }

    @Override
    public RuleVisitor<Object[], String> visitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitPattern(PatternRule pattern, String p) {
                return new Object[] { pattern, p };
            }
        };
    }

    @Test
    @DisplayName("builder(st:String) [Null parameter]")
    void builderStInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> PatternRule.builder((String) null));
    }

    @Test
    @DisplayName("builder(pa:Pattern) [Null parameter]")
    void builderPaInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> PatternRule.builder((Pattern) null));
    }

    @Test
    @DisplayName("builder(st:String) [Invalid regex]")
    void builderStInCaseInvalidRegex() throws Exception {
        assertThrows(PatternSyntaxException.class, () -> PatternRule.builder("["));
    }

    @Test
    @DisplayName("builder(st:String)")
    void builderSt() throws Exception {
        PatternRule.builder("");
    }

    @Test
    @DisplayName("builder(pa:Pattern)")
    void builderPa() throws Exception {
        PatternRule.builder(Pattern.compile(""));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = { "test", "[0-9]" })
    @DisplayName("getPattern()")
    void getPattern(String regex) throws Exception {
        var pattern = PatternRule.builder(regex).build(Stubs.DUMMY_PRODUCTION_SET);

        assertEquals(regex, pattern.getPattern().pattern());
    }
}
