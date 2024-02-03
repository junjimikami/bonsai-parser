package com.unitedjiga.common.parsing.grammar;

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

import com.unitedjiga.common.parsing.grammar.Rule.Kind;

/**
 * 
 * @author Junji Mikami
 *
 */
class ChoiceRuleTest implements RuleTest {

    @Nested
    class BuilderTest implements RuleTest.BulderTest, QuantifiableTest, ReferenceRelatedTest {

        @Override
        public ChoiceRule.Builder builder() {
            return ChoiceRule.builder().add("");
        }

        @Test
        @DisplayName("build(ps:ProductionSet) [No elements]")
        void buildPsInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.build(null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("exactly(int) [No elements]")
        void exactlyInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.exactly(0))
                    .printStackTrace();
        }

        @Test
        @DisplayName("atLeast(int) [No elements]")
        void atLeastInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.atLeast(0))
                    .printStackTrace();
        }

        @Test
        @DisplayName("range(int, int) [No elements]")
        void rangeInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.range(0, 0))
                    .printStackTrace();
        }

        @Test
        @DisplayName("opt() [No elements]")
        void optInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.opt())
                    .printStackTrace();
        }

        @Test
        @DisplayName("zeroOrMore() [No elements]")
        void zeroOrMoreInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.zeroOrMore())
                    .printStackTrace();
        }

        @Test
        @DisplayName("oneOrMore() [No elements]")
        void oneOrMoreInCaseNoElements() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(IllegalStateException.class, () -> builder.oneOrMore())
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(Rule.Builder) [Null parameter]")
        void addEbInCaseNullParameter() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(NullPointerException.class, () -> builder.add((Rule.Builder) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(String) [Null parameter]")
        void addStInCaseNullParameter() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(NullPointerException.class, () -> builder.add((String) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(eb:Rule.Builder) [Post-build operation]")
        void addEbInCasePostBuild() throws Exception {
            var builder = ChoiceRule.builder()
                    .addEmpty();
            builder.build(Stubs.DUMMY_PRODUCTION_SET);

            assertThrows(IllegalStateException.class, () -> builder.add((Rule.Builder) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String) [Post-build operation]")
        void addStInCasePostBuild() throws Exception {
            var builder = ChoiceRule.builder()
                    .addEmpty();
            builder.build(Stubs.DUMMY_PRODUCTION_SET);

            assertThrows(IllegalStateException.class, () -> builder.add((String) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(eb:Rule.Builder)")
        void addEb() throws Exception {
            var builder = ChoiceRule.builder();

            assertEquals(builder, builder.add(Stubs.DUMMY_RULE_BUILDER));
        }

        @Test
        @DisplayName("add(st:String)")
        void addSt() throws Exception {
            var builder = ChoiceRule.builder();

            assertEquals(builder, builder.add("SYMBOL"));
        }

        @Test
        @DisplayName("addEmpty()")
        void addEmpty() throws Exception {
            var builder = ChoiceRule.builder();

            assertEquals(builder, builder.addEmpty());
        }

        @Nested
        class QuantifierBuilderTest implements QuantifierRuleTest.BuilderTest, ReferenceRelatedTest {
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
                .build(Stubs.DUMMY_PRODUCTION_SET);
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
                .build(Stubs.DUMMY_PRODUCTION_SET);

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
        var choice = builder.build(Stubs.DUMMY_PRODUCTION_SET);

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
        list.stream().forEach(builder::add);
        var choice = builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertIterableEquals(list, choice.getChoices().stream()
                .map(e -> (ReferenceRule) e)
                .map(e -> e.getProduction().getSymbol())
                .toList());
    }

    static Stream<List<String>> getChoicesInCaseContainingreferences() {
        return Stream.of(
                List.of("A"),
                List.of("A", "B"));
    }
}
