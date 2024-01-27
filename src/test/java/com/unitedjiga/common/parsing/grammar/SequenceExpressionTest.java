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

import com.unitedjiga.common.parsing.grammar.Expression.Kind;

class SequenceExpressionTest implements ExpressionTest {

    @Nested
    class BuilderTest implements ExpressionTest.BulderTest, QuantifiableTest, ReferenceRelatedTest {

        @Override
        public SequenceExpression.Builder builder() {
            return SequenceExpression.builder().add("");
        }

        @Test
        @DisplayName("build(ps:ProductionSet) [No elements]")
        void buildPsWithNoElements() throws Exception {
            var builder = SequenceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.build(null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("exactly(int) [No elements]")
        void exactlyWithNoElements() throws Exception {
            var builder = SequenceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.exactly(0))
                    .printStackTrace();
        }

        @Test
        @DisplayName("atLeast(int) [No elements]")
        void atLeastWithNoElements() throws Exception {
            var builder = SequenceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.atLeast(0))
                    .printStackTrace();
        }

        @Test
        @DisplayName("range(int, int) [No elements]")
        void rangeWithNoElements() throws Exception {
            var builder = SequenceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.range(0, 0))
                    .printStackTrace();
        }

        @Test
        @DisplayName("opt() [No elements]")
        void optWithNoElements() throws Exception {
            var builder = SequenceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.opt())
                    .printStackTrace();
        }

        @Test
        @DisplayName("zeroOrMore() [No elements]")
        void zeroOrMoreWithNoElements() throws Exception {
            var builder = SequenceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.zeroOrMore())
                    .printStackTrace();
        }

        @Test
        @DisplayName("oneOrMore() [No elements]")
        void oneOrMoreWithNoElements() throws Exception {
            var builder = SequenceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.oneOrMore())
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(Expression.Builder) [Null parameter]")
        void addEbNull() throws Exception {
            var builder = SequenceExpression.builder();

            assertThrows(NullPointerException.class, () -> builder.add((Expression.Builder) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(String) [Null parameter]")
        void addStNull() throws Exception {
            var builder = SequenceExpression.builder();

            assertThrows(NullPointerException.class, () -> builder.add((String) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(eb:Expression.Builder) [Post-build operation]")
        void addEbPostBuild() throws Exception {
            var builder = SequenceExpression.builder()
                    .add(Stubs.DUMMY_EXPRESSION_BUILDER);
            builder.build(Stubs.DUMMY_PRODUCTION_SET);

            assertThrows(IllegalStateException.class, () -> builder.add((Expression.Builder) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String) [Post-build operation]")
        void addStPostBuild() throws Exception {
            var builder = SequenceExpression.builder()
                    .add(Stubs.DUMMY_EXPRESSION_BUILDER);
            builder.build(Stubs.DUMMY_PRODUCTION_SET);

            assertThrows(IllegalStateException.class, () -> builder.add((String) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(eb:Expression.Builder)")
        void addEb() throws Exception {
            var builder = SequenceExpression.builder();

            assertEquals(builder, builder.add(Stubs.DUMMY_EXPRESSION_BUILDER));
        }

        @Test
        @DisplayName("add(st:String)")
        void addSt() throws Exception {
            var builder = SequenceExpression.builder();

            assertEquals(builder, builder.add("SYMBOL"));
        }

        @Nested
        class QuantifierBuilderTest implements QuantifierExpressionTest.BuilderTest, ReferenceRelatedTest {
            @Override
            public QuantifierExpression.Builder builder() {
                return SequenceExpressionTest.BuilderTest.this.builder().opt();
            }
        }

        @Nested
        class QuantifierTest implements QuantifierExpressionTest {
            @Override
            public Quantifiable builder() {
                return SequenceExpressionTest.BuilderTest.this.builder();
            }
        }
    }

    @Override
    public Expression build() {
        return SequenceExpression.builder()
                .add(Stubs.DUMMY_EXPRESSION_BUILDER)
                .build(Stubs.DUMMY_PRODUCTION_SET);
    }

    @Override
    public Kind kind() {
        return Kind.SEQUENCE;
    }

    @Override
    public ExpressionVisitor<Object[], String> elementVisitor() {
        return new TestExpressionVisitor<Object[], String>() {
            @Override
            public Object[] visitSequence(SequenceExpression sequence, String p) {
                return new Object[] { sequence, p };
            }
        };
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("getSequence()")
    void getSequence(List<Expression> list) throws Exception {
        var builder = SequenceExpression.builder();
        list.stream()
                .map(Stubs::builderOf)
                .forEach(builder::add);
        var sequence = builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertIterableEquals(list, sequence.getSequence());
    }

    static Stream<List<Expression>> getSequence() {
        return Stream.of(
                List.of(Stubs.expression("A")),
                List.of(Stubs.expression("A"), Stubs.expression("B")));
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("getSequence() [Containing references]")
    void getSequenceContainingReference(List<String> list) throws Exception {
        var builder = SequenceExpression.builder();
        list.stream().forEach(builder::add);
        var sequence = builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertIterableEquals(list, sequence.getSequence().stream()
                .map(e -> (ReferenceExpression) e)
                .map(e -> e.getProduction().getSymbol())
                .toList());
    }

    static Stream<List<String>> getSequenceContainingReference() {
        return Stream.of(
                List.of("A"),
                List.of("A", "B"));
    }

}
