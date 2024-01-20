package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.unitedjiga.common.parsing.grammar.Expression.Kind;

class QuantifierExpressionTest implements ExpressionTest {

    @Nested
    class BuilderTest {

        private Stream<ExpressionTest.BulderTest> builderTests() {
            return Stream.of(
                    () -> ChoiceExpression.builder().add("").opt(),
                    () -> SequenceExpression.builder().add("").opt(),
                    () -> PatternExpression.builder("").opt(),
                    () -> ReferenceExpression.builder("").opt());
        }

        @TestFactory
        Stream<DynamicNode> buildInCasePostBuild() throws Exception {
            return builderTests().map(test -> dynamicTest(
                    "build() [Post-build operation]",
                    test::buildInCasePostBuild));
        }

        @TestFactory
        Stream<DynamicNode> buildPsInCasePostBuild() throws Exception {
            return builderTests().map(test -> dynamicTest(
                    "build(ps:ProductionSet) [Post-build operation]",
                    test::buildPsInCasePostBuild));
        }

        private Stream<ReferenceRelatedTest> referenceRelatedTest() {
            return Stream.of(
                    () -> ChoiceExpression.builder().add("").opt(),
                    () -> SequenceExpression.builder().add("").opt(),
                    () -> ReferenceExpression.builder("").opt());
        }

        @TestFactory
        Stream<DynamicNode> buildInCaseNullProductionSet() throws Exception {
            return referenceRelatedTest().map(test -> dynamicTest(
                    "build() [Null production set]",
                    test::buildInCaseNullProductionSet));
        }

        @TestFactory
        Stream<DynamicNode> buildPsInCaseNullProductionSet() throws Exception {
            return referenceRelatedTest().map(test -> dynamicTest(
                    "build(ps:ProductionSet) [Null production set]",
                    test::buildPsInCaseNullProductionSet));
        }

        @TestFactory
        Stream<DynamicNode> buildPsInCaseInvalidReference() throws Exception {
            return referenceRelatedTest().map(test -> dynamicTest(
                    "build(ps:ProductionSet) [Invalid production set]",
                    test::buildPsInCaseInvalidReference));
        }
    }

    @Override
    public Expression build() {
        return PatternExpression.builder("").opt().build();
    }

    @Test
    @DisplayName("getKind()")
    void getKind() throws Exception {
        var quantifier = build();

        assertEquals(Kind.QUANTIFIER, quantifier.getKind());
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor)")
    void acceptEv() throws Exception {
        var quantifier = build();

        quantifier.accept(new TestExpressionVisitor<Void, Object>() {
            @Override
            public Void visitQuantifier(QuantifierExpression expression, Object p) {
                assertEquals(quantifier, expression);
                assertNull(p);
                return null;
            }
        });
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor, p:P)")
    void acceptEvP() throws Exception {
        var quantifier = build();
        var arg = new Object();

        quantifier.accept(new TestExpressionVisitor<Void, Object>() {
            @Override
            public Void visitQuantifier(QuantifierExpression expression, Object p) {
                assertEquals(quantifier, expression);
                assertEquals(arg, p);
                return null;
            }
        }, arg);
    }

    @Test
    @DisplayName("opt()")
    void opt() throws Exception {
        var quantifier = PatternExpression.builder("").opt().build();

        assertEquals(0, quantifier.getLowerLimit());
        assertEquals(1, quantifier.getUpperLimit().getAsInt());
        assertEquals(1, quantifier.stream().count());
    }

    @Test
    @DisplayName("zeroOrMore()")
    void zeroOrMore() throws Exception {
        var quantifier = PatternExpression.builder("").zeroOrMore().build();

        assertEquals(0, quantifier.getLowerLimit());
        assertEquals(true, quantifier.getUpperLimit().isEmpty());
    }

    @Test
    @DisplayName("oneOrMore()")
    void oneOrMore() throws Exception {
        var quantifier = PatternExpression.builder("").oneOrMore().build();

        assertEquals(1, quantifier.getLowerLimit());
        assertEquals(true, quantifier.getUpperLimit().isEmpty());
    }

    @Test
    @DisplayName("exactly()")
    void exactly() throws Exception {
        var quantifier = PatternExpression.builder("").exactly(0).build();

        assertEquals(0, quantifier.getLowerLimit());
        assertEquals(0, quantifier.getUpperLimit().getAsInt());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2 })
    @DisplayName("atLeast(int)")
    void atLeast(int times) throws Exception {
        var quantifier = PatternExpression.builder("").atLeast(times).build();

        assertEquals(times, quantifier.getLowerLimit());
        assertEquals(true, quantifier.getUpperLimit().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "0,0", "0,1", "0,2",
            "1,1", "1,2",
    })
    @DisplayName("range()")
    void range(int min, int max) throws Exception {
        var quantifier = PatternExpression.builder("").range(min, max).build();

        assertEquals(min, quantifier.getLowerLimit());
        assertEquals(max, quantifier.getUpperLimit().getAsInt());
    }

}
