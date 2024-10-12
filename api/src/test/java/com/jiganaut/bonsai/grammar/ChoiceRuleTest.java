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
class ChoiceRuleTest implements CompositeRuleTest<ChoiceRule> {

    @Nested
    class BuilderTest implements CompositeRuleTest.BuilderTest<ChoiceRule.Builder> {

        @Override
        public ChoiceRule.Builder createTarget() {
            return ChoiceRule.builder().add(() -> ReferenceRule.of(""));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder) [Null parameter]")
        void addrbInCaseOfNullParameter() throws Exception {
            var builder = ChoiceRule.builder();

            assertThrows(NullPointerException.class, () -> builder.add((Rule.Builder) null));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder) [Post-build operation]")
        void addrbInCaseOfPostBuild() throws Exception {
            var builder = ChoiceRule.builder()
                    .addEmpty();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add((Rule.Builder) null));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder)")
        void addrb() throws Exception {
            var builder = ChoiceRule.builder();

            assertEquals(builder, builder.add(Stubs.DUMMY_RULE_BUILDER));
        }

        @Test
        @DisplayName("addEmpty()")
        void addEmpty() throws Exception {
            var builder = ChoiceRule.builder();

            assertEquals(builder, builder.addEmpty());
        }

    }

    @Override
    public ChoiceRule createTarget() {
        return ChoiceRule.builder()
                .addEmpty()
                .build();
    }

    @Override
    public Kind expectedKind() {
        return Kind.CHOICE;
    }

    @Override
    public RuleVisitor<Object[], String> createVisitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitChoice(ChoiceRule choice, String p) {
                return new Object[] { choice, p };
            }
        };
    }

    @Test
    @DisplayName("getChoices() [Containing empty]")
    void getChoicesInCaseOfContainingEmpty() throws Exception {
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
    void getChoicesInCaseOfContainingreferences(List<String> list) throws Exception {
        var builder = ChoiceRule.builder();
        list.stream().forEach(symbol -> builder.add(() -> ReferenceRule.of(symbol)));
        var choice = builder.build();

        assertIterableEquals(list, choice.getChoices().stream()
                .map(e -> (ReferenceRule) e)
                .map(e -> e.getProduction(Stubs.DUMMY_PRODUCTION_SET).getSymbol())
                .toList());
    }

    static Stream<List<String>> getChoicesInCaseOfContainingreferences() {
        return Stream.of(
                List.of("A"),
                List.of("A", "B"));
    }
}
