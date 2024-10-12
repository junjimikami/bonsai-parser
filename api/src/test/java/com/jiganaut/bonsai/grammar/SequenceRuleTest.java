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

class SequenceRuleTest implements CompositeRuleTest<SequenceRule> {

    @Nested
    class BuilderTest implements CompositeRuleTest.BuilderTest<SequenceRule.Builder> {

        @Override
        public SequenceRule.Builder createTarget() {
            return SequenceRule.builder().add(() -> ReferenceRule.of(""));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder) [Null parameter]")
        void addRbInCaseOfNullParameter() throws Exception {
            var builder = SequenceRule.builder();

            assertThrows(NullPointerException.class, () -> builder.add((Rule.Builder) null));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder) [Post-build operation]")
        void addRbInCaseOfPostBuild() throws Exception {
            var builder = SequenceRule.builder()
                    .add(Stubs.DUMMY_RULE_BUILDER);
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add((Rule.Builder) null));
        }

        @Test
        @DisplayName("add(rb:Rule.Builder)")
        void addRb() throws Exception {
            var builder = SequenceRule.builder();

            assertEquals(builder, builder.add(Stubs.DUMMY_RULE_BUILDER));
        }

    }

    @Override
    public SequenceRule createTarget() {
        return SequenceRule.builder()
                .add(Stubs.DUMMY_RULE_BUILDER)
                .build();
    }

    @Override
    public Kind expectedKind() {
        return Kind.SEQUENCE;
    }

    @Override
    public RuleVisitor<Object[], String> createVisitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitSequence(SequenceRule sequence, String p) {
                return new Object[] { sequence, p };
            }
        };
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("getRules()")
    void getRules(List<Rule> list) throws Exception {
        var builder = SequenceRule.builder();
        list.stream()
                .map(Stubs::builderOf)
                .forEach(builder::add);
        var sequence = builder.build();

        assertIterableEquals(list, sequence.getRules());
    }

    static Stream<List<Rule>> getRules() {
        return Stream.of(
                List.of(Stubs.rule("A")),
                List.of(Stubs.rule("A"), Stubs.rule("B")));
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("getRules() [Containing references]")
    void getRulesInCaseContainingReference(List<String> list) throws Exception {
        var builder = SequenceRule.builder();
        list.stream().forEach(symbol -> builder.add(() -> ReferenceRule.of(symbol)));
        var sequence = builder.build();

        assertIterableEquals(list, sequence.getRules().stream()
                .map(e -> (ReferenceRule) e)
                .map(e -> e.getProduction(Stubs.DUMMY_PRODUCTION_SET).getSymbol())
                .toList());
    }

    static Stream<List<String>> getRulesInCaseContainingReference() {
        return Stream.of(
                List.of("A"),
                List.of("A", "B"));
    }

}
