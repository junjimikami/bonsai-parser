package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.GrammarMockFactory.mockRule;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

/**
 *
 * @author Junji Mikami
 */
class SkipRuleTest {

    @Test
    @DisplayName("of(r:Rule) [r == null]")
    void ofWhenRIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> SkipRule.of(null));
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("of(Rule)")
    void of() throws Exception {
        Rule<?> rule = mockRule();
        var target = SkipRule.of(rule);

        assertNotNull(target);
        assertEquals(rule, target.getRule());
    }

    @Nested
    class MatchingRuleTestCase {

        @Nested
        class TestCase1 implements SkipRuleTestCase<String> {

            @Override
            public SkipRule<String> createTarget() {
                return Rules.pattern("\\s").skip();
            }

            @Override
            public Skippable<String> expectedRule() {
                return Rules.pattern("\\s");
            }

        }

    }

    @Nested
    class ChoiceRuleTestCase {

        @Nested
        class TestCase1 implements SkipRuleTestCase<String> {

            @Override
            public SkipRule<String> createTarget() {
                return ChoiceRule.<String>builder().build().skip();
            }

            @Override
            public Skippable<String> expectedRule() {
                return ChoiceRule.<String>builder().build();
            }

        }

        @Nested
        class BuilderTestCase1 implements SkipRuleTestCase.BuilderTestCase<String> {

            @Override
            public SkipRule.Builder<String> createTarget() {
                return ChoiceRule.<String>builder().skip();
            }

            @Override
            public SkipRule<String> expectedRule() {
                return ChoiceRule.<String>builder().skip().build();
            }

        }
    }
}
