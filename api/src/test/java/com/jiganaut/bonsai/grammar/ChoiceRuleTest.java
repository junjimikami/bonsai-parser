package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 * 
 * @author Junji Mikami
 *
 */
class ChoiceRuleTest implements RuleTest {

    @Nested
    class BuilderTest implements RuleTest.BulderTest, QuantifiableTest {

        @Override
        public ChoiceRule.Builder builder() {
            return ChoiceRule.builder().add(ReferenceRule.builder(""));
        }

        @Test
        @DisplayName("build() [No elements]")
        void buildInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.build());
        }

        @Test
        @DisplayName("exactly(int) [No elements]")
        void exactlyInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.exactly(0));
        }

        @Test
        @DisplayName("atLeast(int) [No elements]")
        void atLeastInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.atLeast(0));
        }

        @Test
        @DisplayName("range(int, int) [No elements]")
        void rangeInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.range(0, 0));
        }

        @Test
        @DisplayName("opt() [No elements]")
        void optInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.opt());
        }

        @Test
        @DisplayName("zeroOrMore() [No elements]")
        void zeroOrMoreInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.zeroOrMore());
        }

        @Test
        @DisplayName("oneOrMore() [No elements]")
        void oneOrMoreInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.oneOrMore());
        }

        @Test
        @DisplayName("add(Rule.Builder) [Null parameter]")
        void addEbInCaseNullParameter() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(NullPointerException.class, () -> builder.add((Rule.Builder) null));
        }

        @Test
        @DisplayName("add(eb:Rule.Builder) [Post-build operation]")
        void addEbInCasePostBuild() throws Exception {
            var builder = ChoiceRule.builder()
                    .addEmpty();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add((Rule.Builder) null));
        }

        @Test
        @DisplayName("add(eb:Rule.Builder)")
        void addEb() throws Exception {
            var builder = ChoiceRule.builder();

            assertEquals(builder, builder.add(Stubs.DUMMY_RULE_BUILDER));
        }

        @Test
        @DisplayName("addEmpty()")
        void addEmpty() throws Exception {
            var builder = ChoiceRule.builder();

            assertEquals(builder, builder.addEmpty());
        }

        @Nested
        class QuantifierBuilderTest implements QuantifierRuleTest.BuilderTest {
            @Override
            public QuantifierRule.Builder builder() {
                return ChoiceRuleTest.BuilderTest.this.builder().opt();
            }
        }

        @Nested
        class QuantifierTest implements QuantifierRuleTest {
            @Override
            public Quantifiable builder() {
                return ChoiceRuleTest.BuilderTest.this.builder();
            }
        }

    }

    @Override
    public Rule build() {
        return ChoiceRule.builder()
                .addEmpty()
                .build();
    }

    @Override
    public Kind kind() {
        return Kind.CHOICE;
    }

    @Override
    public RuleVisitor<Object[], String> visitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitChoice(ChoiceRule choice, String p) {
                return new Object[] { choice, p };
            }
        };
    }

    @Test
    @DisplayName("getChoices() [Containing empty]")
    void getChoicesInCaseContainingEmpty() throws Exception {
        var choice = ChoiceRule.builder()
                .addEmpty()
                .build();

        assertIterableEquals(List.of(Rule.EMPTY), choice.getChoices());
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("getChoices()")
    void getChoices(List<Rule> list) throws Exception {
        var builder = ChoiceRule.builder();
        list.stream()
                .map(Stubs::builderOf)
                .forEach(builder::add);
        var choice = builder.build();

        assertIterableEquals(list, choice.getChoices());
    }

    static Stream<List<Rule>> getChoices() {
        return Stream.of(
                List.of(Stubs.rule("A")),
                List.of(Stubs.rule("A"), Stubs.rule("B")));
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("getChoices() [Containing references]")
    void getChoicesInCaseContainingreferences(List<String> list) throws Exception {
        var builder = ChoiceRule.builder();
        list.stream().forEach(symbol -> builder.add(ReferenceRule.builder(symbol)));
        var choice = builder.build();

        assertIterableEquals(list, choice.getChoices().stream()
                .map(e -> (ReferenceRule) e)
                .map(e -> e.getProduction(Stubs.DUMMY_PRODUCTION_SET).getSymbol())
                .toList());
    }

    static Stream<List<String>> getChoicesInCaseContainingreferences() {
        return Stream.of(
                List.of("A"),
                List.of("A", "B"));
    }
}
