package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.unitedjiga.common.parsing.grammar.Expression.Kind;

/**
 * 
 * @author Junji Mikami
 *
 */
class ChoiceExpressionTest implements ExpressionTest {

    @Nested
    class BuilderTest implements ExpressionTest.BulderTest, QuantifiableTest, ReferenceRelatedTest {

        @Override
        public ChoiceExpression.Builder builder() {
            return ChoiceExpression.builder().add("");
        }

        @Test
        @DisplayName("build() [No elements]")
        void buildInCaseNoElements() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.build())
                    .printStackTrace();
        }

        @Test
        @DisplayName("build(ps:ProductionSet) [No elements]")
        void buildPsInCaseNoElements() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.build(null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("exactly(int) [No elements]")
        void exactlyInCaseNoElements() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.exactly(0))
                    .printStackTrace();
        }

        @Test
        @DisplayName("atLeast(int) [No elements]")
        void atLeastInCaseNoElements() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.atLeast(0))
                    .printStackTrace();
        }

        @Test
        @DisplayName("range(int, int) [No elements]")
        void rangeInCaseNoElements() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.range(0, 0))
                    .printStackTrace();
        }

        @Test
        @DisplayName("opt() [No elements]")
        void optInCaseNoElements() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.opt())
                    .printStackTrace();
        }

        @Test
        @DisplayName("zeroOrMore() [No elements]")
        void zeroOrMoreInCaseNoElements() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.zeroOrMore())
                    .printStackTrace();
        }

        @Test
        @DisplayName("oneOrMore() [No elements]")
        void oneOrMoreInCaseNoElements() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(IllegalStateException.class, () -> builder.oneOrMore())
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(Expression.Builder) [Null parameter]")
        void addEbInCaseNullParameter() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(NullPointerException.class, () -> builder.add((Expression.Builder) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(String) [Null parameter]")
        void addStInCaseNullParameter() throws Exception {
            var builder = ChoiceExpression.builder();

            assertThrows(NullPointerException.class, () -> builder.add((String) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(eb:Expression.Builder) [Post-build operation]")
        void addEbInCasePostBuild() throws Exception {
            var builder = ChoiceExpression.builder()
                    .addEmpty();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add((Expression.Builder) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String) [Post-build operation]")
        void addStInCasePostBuild() throws Exception {
            var builder = ChoiceExpression.builder()
                    .addEmpty();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add((String) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(eb:Expression.Builder)")
        void addEb() throws Exception {
            var builder = ChoiceExpression.builder();

            assertEquals(builder, builder.add(Stubs.DUMMY_EXPRESSION_BUILDER));
        }

        @Test
        @DisplayName("add(st:String)")
        void addSt() throws Exception {
            var builder = ChoiceExpression.builder();

            assertEquals(builder, builder.add("SYMBOL"));
        }

        @Test
        @DisplayName("addEmpty()")
        void addEmpty() throws Exception {
            var builder = ChoiceExpression.builder();

            assertEquals(builder, builder.addEmpty());
        }

    }

    @Override
    public Expression build() {
        return ChoiceExpression.builder()
                .addEmpty()
                .build();
    }

    @Test
    @DisplayName("getKind()")
    void getKind() throws Exception {
        var choice = build();

        assertEquals(Kind.CHOICE, choice.getKind());
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor)")
    void acceptEv() throws Exception {
        var choice = build();

        choice.accept(new TestExpressionVisitor<Void, Object>() {
            @Override
            public Void visitChoice(ChoiceExpression expression, Object p) {
                assertEquals(choice, expression);
                assertNull(p);
                return null;
            }
        });
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor, p:P)")
    void acceptEvP() throws Exception {
        var choice = build();
        var arg = new Object();

        choice.accept(new TestExpressionVisitor<Void, Object>() {
            @Override
            public Void visitChoice(ChoiceExpression expression, Object p) {
                assertEquals(choice, expression);
                assertEquals(arg, p);
                return null;
            }
        }, arg);
    }

    @Test
    @DisplayName("getChoices() [Containing empty]")
    void getChoicesInCaseContainingEmpty() throws Exception {
        var choice = ChoiceExpression.builder()
                .addEmpty()
                .build();

        assertIterableEquals(List.of(Expression.EMPTY), choice.getChoices());
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("getChoices()")
    void getChoices(List<Expression> list) throws Exception {
        var builder = ChoiceExpression.builder();
        list.stream()
        .map(Stubs::builderOf)
        .forEach(builder::add);
        var choice = builder.build();
        
        assertIterableEquals(list, choice.getChoices());
    }
    
    static Stream<List<Expression>> getChoices() {
        return Stream.of(
                List.of(Stubs.expression("A")),
                List.of(Stubs.expression("A"), Stubs.expression("B")));
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("getChoices() [Containing references]")
    void getChoicesInCaseContainingreferences(List<String> list) throws Exception {
        var builder = ChoiceExpression.builder();
        list.stream().forEach(builder::add);
        var choice = builder.build(Stubs.DUMMY_PRODUCTION_SET);

        assertIterableEquals(list, choice.getChoices().stream()
                .map(e -> (ReferenceExpression) e)
                .map(e -> e.get().getSymbol())
                .toList());
    }

    static Stream<List<String>> getChoicesInCaseContainingreferences() {
        return Stream.of(
                List.of("A"),
                List.of("A", "B"));
    }
}
