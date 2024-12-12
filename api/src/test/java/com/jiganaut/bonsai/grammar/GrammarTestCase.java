package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;

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
interface GrammarTestCase extends ProductionSetTestCase {

    interface BuilderTestCase extends TestCase {

        @Override
        Grammar.Builder createTarget();

        Set<Production> expectedProductionSet();

        default boolean isNoElements() {
            return expectedProductionSet().isEmpty();
        }

        default boolean isContainingInvalidReference() {
            var expected = expectedProductionSet();
            if (expected.isEmpty()) {
                return false;
            }
            var references = expected.stream()
                    .<ReferenceRule>mapMulti((e, consumer) -> {
                        if (e.getRule() instanceof ReferenceRule r) {
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

        default boolean isContainingRulesWithNoElements() {
            var expected = expectedProductionSet();
            if (expected.isEmpty()) {
                return false;
            }
            return expected.stream()
                    .<Rule>mapMulti((e, consumer) -> {
                        if (e.getRule() instanceof ChoiceRule r
                                && r.getChoices().isEmpty()) {
                            consumer.accept(r);
                        } else if (e.getRule() instanceof SequenceRule r
                                && r.getRules().isEmpty()) {
                            consumer.accept(r);
                        }
                    })
                    .findAny()
                    .isPresent();
        }

        default boolean isContainingBuildersReturningNull() {
            var expected = expectedProductionSet();
            if (expected.isEmpty()) {
                return false;
            }
            return expected.stream()
                    .filter(e -> e.getRule() == null)
                    .findAny()
                    .isPresent();
        }

        default boolean canBuild() {
            return !isNoElements()
                    && !isContainingInvalidReference()
                    && !isContainingRulesWithNoElements()
                    && !isContainingBuildersReturningNull();
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, ru:Rule) [Null parameter]")
        default void addStRuInCaseOfNullParameter(TestReporter testReporter) throws Exception {
            var builder = createTarget();

            var ex0 = assertThrows(NullPointerException.class, () -> builder.add(null, mock(Rule.class)));
            testReporter.publishEntry(ex0.getMessage());
            var ex1 = assertThrows(NullPointerException.class, () -> builder.add("", (Rule) null));
            testReporter.publishEntry(ex1.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, rb:Rule.Builder) [Null parameter]")
        default void addStRbInCaseOfNullParameter(TestReporter testReporter) throws Exception {
            var builder = createTarget();

            var ex0 = assertThrows(NullPointerException.class, () -> builder.add(null, mock(Rule.Builder.class)));
            testReporter.publishEntry(ex0.getMessage());
            var ex1 = assertThrows(NullPointerException.class, () -> builder.add("", (Rule.Builder) null));
            testReporter.publishEntry(ex1.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, ru:Rule) [Post-build operation]")
        default void addStRuInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.add("", mock(Rule.class)));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, rb:Rule.Builder) [Post-build operation]")
        default void addRtbInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.add("", mock(Rule.Builder.class)));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Post-build operation]")
        default void buildInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.build());
            testReporter.publishEntry(ex.getMessage());
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = { "1", "a", "[" })
        @DisplayName("add(st:String, ru:Rule)")
        default void addStRu(String s) throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.add(s, mock(Rule.class)));
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = { "1", "a", "[" })
        @DisplayName("add(st:String, rb:Rule.Builder)")
        default void addStRb(String s) throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.add(s, mock(Rule.Builder.class)));
        }

        @Test
        @DisplayName("build()")
        default void build() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            var productionSet = builder.build();

            assertNotNull(productionSet);
            var expectedString = expectedProductionSet().stream()
                    .map(e -> e.getSymbol() + ":" + e.getRule())
                    .sorted()
                    .collect(Collectors.joining(",", "{", "}"));
            var actualString = productionSet.stream()
                    .map(e -> e.getSymbol() + ":" + e.getRule())
                    .sorted()
                    .collect(Collectors.joining(",", "{", "}"));
            assertEquals(expectedString, actualString);
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [No elements]")
        default void buildInCaseOfNoElements(TestReporter testReporter) throws Exception {
            assumeTrue(isNoElements());

            var builder = createTarget();

            var ex = assertThrows(IllegalStateException.class, () -> builder.build());
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Containing invalid reference]")
        default void buildInCaseOfContainingInvalidReference(TestReporter testReporter) throws Exception {
            assumeTrue(isContainingInvalidReference());

            var builder = createTarget();

            var ex = assertThrows(NoSuchElementException.class, () -> builder.build());
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Containing rules with no elements]")
        default void buildInCaseOfContainingRulesWithNoElements(TestReporter testReporter) throws Exception {
            assumeTrue(isContainingRulesWithNoElements());

            var builder = createTarget();

            var ex = assertThrows(IllegalStateException.class, () -> builder.build());
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Containing builders returning null]")
        default void buildInCaseOfContainingBuildersReturningNull(TestReporter testReporter) throws Exception {
            assumeTrue(isContainingBuildersReturningNull());

            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.build());
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("shortCircuit() [No elements]")
        default void shortCircuitInCaseOfNoElements(TestReporter testReporter) throws Exception {
            assumeTrue(isNoElements());

            var builder = createTarget();

            var ex = assertThrows(IllegalStateException.class, () -> builder.shortCircuit());
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("shortCircuit() [Post-build operation]")
        default void shortCircuitInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.shortCircuit());
            testReporter.publishEntry(ex.getMessage());
        }

        @Test
        @DisplayName("shortCircuit()")
        default void shortCircuit() throws Exception {
            assumeTrue(canBuild());

            var target = createTarget().shortCircuit();

            assertTrue(target.isShortCircuit());
        }

    }

    @Override
    Grammar createTarget();

    Set<Production> expectedProductionSet();

    @Test
    @DisplayName("productionSet()")
    default void productionSet() throws Exception {
        var target = createTarget();

        var expectedString = expectedProductionSet().stream()
                .map(e -> e.getSymbol() + ":" + e.getRule())
                .sorted()
                .collect(Collectors.joining(",", "{", "}"));
        var actualString = target.productionSet().stream()
                .map(e -> e.getSymbol() + ":" + e.getRule())
                .sorted()
                .collect(Collectors.joining(",", "{", "}"));
        assertEquals(expectedString, actualString);
        assertTrue(target.productionSet().shortCircuit().isShortCircuit());
    }

}
