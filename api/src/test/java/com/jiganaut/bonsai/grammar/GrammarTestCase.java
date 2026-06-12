package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.MockFactory.mockRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockRuleBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jiganaut.bonsai.TestCase;

/**
 *
 * @author Junji Mikami
 *
 */
interface GrammarTestCase<T> extends TestCase {

    interface BuilderTestCase<T> extends TestCase {

        @Override
        Grammar.Builder<T> createTarget();

        Set<ProductionRule<T>> expectedProductionRules();

        default boolean isContainingInvalidReference() {
            var expected = expectedProductionRules();
            if (expected.isEmpty()) {
                return false;
            }
            var references = expected.stream()
                    .<ReferenceRule<T>>mapMulti((e, consumer) -> {
                        if (e.getRule() instanceof ReferenceRule<T> r) {
                            consumer.accept(r);
                        }
                    })
                    .toList();
            if (references.isEmpty()) {
                return false;
            }
            return references.stream()
                    .map(e -> e.getSymbol())
                    .noneMatch(e -> expected.stream()
                            .map(e2 -> e2.getSymbol())
                            .anyMatch(e2 -> e2.equals(e)));
        }

        default boolean canBuild() {
            return !isContainingInvalidReference();
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, ru:Rule) [st == null]")
        default void addStRuWhenStIsNull(TestReporter testReporter) throws Exception {
            var target = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> target.add(null, mockRule()));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, rb:Rule.Builder) [st == null]")
        default void addStRbWhenStIsNull(TestReporter testReporter) throws Exception {
            var target = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> target.add(null, mockRuleBuilder()));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, rb:Rule.Builder) [rb == null]")
        default void addStRbWhenRbIsNull(TestReporter testReporter) throws Exception {
            var target = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> target.add("", (Rule.Builder<T>) null));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, ru:Rule) [Post-build]")
        default void addStRuWhenPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var target = createTarget();
            target.build();

            var ex = assertThrows(IllegalStateException.class, () -> target.add("", mockRule()));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, rb:Rule.Builder) [Post-build]")
        default void addStRbWhenPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var target = createTarget();
            target.build();

            var ex = assertThrows(IllegalStateException.class, () -> target.add("", mockRuleBuilder()));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Post-build]")
        default void buildWhenPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var target = createTarget();
            target.build();

            var ex = assertThrows(IllegalStateException.class, () -> target.build());
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = { "1", "a", "[" })
        @DisplayName("add(st:String, ru:Rule)")
        default void addStRu(String s) throws Exception {
            var target = createTarget();

            assertEquals(target, target.add(s, mockRule()));
        }

        @Test
        @DisplayName("add(st:String, ru:Rule) [ru == null]")
        default void addStRuWhenRuIsNull() throws Exception {
            var target = createTarget();

            assertEquals(target, target.add("1", (Rule<T>) null));
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = { "1", "a", "[" })
        @DisplayName("add(st:String, rb:Rule.Builder)")
        default void addStRb(String s) throws Exception {
            var target = createTarget();

            assertEquals(target, target.add(s, mockRuleBuilder()));
        }

        @Test
        @DisplayName("build()")
        default void build() throws Exception {
            assumeTrue(canBuild());

            var target = createTarget();
            var productionSet = target.build();

            assertNotNull(productionSet);
            var expectedString = expectedProductionRules().stream()
                    .map(e -> e.getSymbol() + ":" + e.getRule())
                    .sorted()
                    .collect(Collectors.joining(",", "{", "}"));
            var actualString = productionSet.getProductionRules().stream()
                    .map(e -> e.getSymbol() + ":" + e.getRule())
                    .sorted()
                    .collect(Collectors.joining(",", "{", "}"));
            assertEquals(expectedString, actualString);
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Containing invalid reference]")
        default void buildWhenContainingInvalidReference(TestReporter testReporter) throws Exception {
            assumeTrue(isContainingInvalidReference());

            var target = createTarget();

            var ex = assertThrows(NoSuchElementException.class, () -> target.build());
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("asShortCircuit() [Post-build]")
        default void asShortCircuitWhenPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var target = createTarget();
            target.build();

            var ex = assertThrows(IllegalStateException.class, () -> target.asShortCircuit());
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @Test
        @DisplayName("asShortCircuit()")
        default void asShortCircuit() throws Exception {
            assumeTrue(canBuild());

            var target = createTarget().asShortCircuit().build();

            assertTrue(target.isShortCircuit());
        }

    }

    @Override
    Grammar<T> createTarget();

    default String expectedStartSymbol() {
        return null;
    }

    Set<ProductionRule<T>> expectedProductionRules();

    default boolean expectedShortCircuit() {
        return false;
    }

    default ChoiceRule<T> expectedChoiceRule() {
        var builder = ChoiceRule.<T>builder();
        expectedProductionRules().forEach(e -> builder.add(e.getRule()));
        return builder.build();
    }

    @Test
    @DisplayName("getStartSymbol()")
    default void getStartSymbol() throws Exception {
        var target = createTarget();

        assertEquals(expectedStartSymbol(), target.getStartSymbol());
    }

    @Test
    @DisplayName("getProductionRules()")
    default void getProductionRules() throws Exception {
        var target = createTarget();

        var expected = expectedProductionRules().stream()
                .map(e -> List.of(e.getSymbol(), e.getRule()))
                .collect(Collectors.toSet());
        var actual = target.getProductionRules().stream()
                .map(e -> List.of(e.getSymbol(), e.getRule()))
                .collect(Collectors.toSet());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("isShortCircuit()")
    default void isShortCircuit() throws Exception {
        var target = createTarget();

        assertEquals(expectedShortCircuit(), target.isShortCircuit());
    }

    @Test
    @DisplayName("toChoiceRule()")
    default void toChoiceRule() throws Exception {
        var target = createTarget();

        var expected = expectedProductionRules().stream()
                .filter(e -> expectedStartSymbol() == null || expectedStartSymbol().equals(e.getSymbol()))
                .map(e -> (ProductionRule<?>) e)
                .map(e -> List.of(e.getSymbol(), e.getRule()))
                .collect(Collectors.toSet());
        var actual = target.toChoiceRule().getChoices().stream()
                .map(e -> (ProductionRule<?>) e)
                .map(e -> List.of(e.getSymbol(), e.getRule()))
                .collect(Collectors.toSet());
        assertEquals(expected, actual);
    }

}
