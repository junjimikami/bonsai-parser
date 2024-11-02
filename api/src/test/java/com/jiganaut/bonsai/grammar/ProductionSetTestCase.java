package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jiganaut.bonsai.TestCase;

/**
 * 
 * @author Junji Mikami
 *
 */
interface ProductionSetTestCase extends TestCase {

    interface BuilderTestCase extends TestCase {

        @Override
        ProductionSet.Builder createTarget();

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

        @Test
        @DisplayName("add(st:String, ru:Rule) [Null parameter]")
        default void addStRuInCaseOfNullParameter() throws Exception {
            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.add(null, mock(Rule.class)));
            assertThrows(NullPointerException.class, () -> builder.add("", (Rule) null));
        }

        @Test
        @DisplayName("add(st:String, rb:Rule.Builder) [Null parameter]")
        default void addStRbInCaseOfNullParameter() throws Exception {
            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.add(null, mock(Rule.Builder.class)));
            assertThrows(NullPointerException.class, () -> builder.add("", (Rule.Builder) null));
        }

        @Test
        @DisplayName("add(st:String, ru:Rule) [Post-build operation]")
        default void addStRuInCaseOfPostBuild() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add("", mock(Rule.class)));
        }

        @Test
        @DisplayName("add(st:String, rb:Rule.Builder) [Post-build operation]")
        default void addRtbInCaseOfPostBuild() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add("", mock(Rule.Builder.class)));
        }

        @Test
        @DisplayName("build() [Post-build operation]")
        default void buildInCaseOfPostBuild() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.build());
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

        @Test
        @DisplayName("build() [No elements]")
        default void buildInCaseOfNoElements() throws Exception {
            assumeTrue(isNoElements());

            var builder = createTarget();

            assertThrows(IllegalStateException.class, () -> builder.build());
        }

        @Test
        @DisplayName("build() [Containing invalid reference]")
        default void buildInCaseOfContainingInvalidReference() throws Exception {
            assumeTrue(isContainingInvalidReference());

            var builder = createTarget();

            assertThrows(NoSuchElementException.class, () -> builder.build());
        }

        @Test
        @DisplayName("build() [Containing rules with no elements]")
        default void buildInCaseOfContainingRulesWithNoElements() throws Exception {
            assumeTrue(isContainingRulesWithNoElements());

            var builder = createTarget();

            assertThrows(IllegalStateException.class, () -> builder.build());
        }

        @Test
        @DisplayName("build() [Containing builders returning null]")
        default void buildInCaseOfContainingBuildersReturningNull() throws Exception {
            assumeTrue(isContainingBuildersReturningNull());

            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.build());
        }

    }

    @Override
    ProductionSet createTarget();

    Set<Production> expectedProductionSet();

    @Test
    @DisplayName("containsSymbol(String)")
    default void containsSymbol() throws Exception {
        var target = createTarget();

        for (var production : expectedProductionSet()) {
            var symbol = production.getSymbol();
            assertTrue(target.containsSymbol(symbol));
        }
    }

    @Test
    @DisplayName("getProduction(String)")
    default void getProduction() throws Exception {
        var target = createTarget();

        var map = expectedProductionSet().stream()
                .collect(Collectors.groupingBy(e -> e.getSymbol()));
        map.forEach((symbol, list) -> {
            var actual = target.getProduction(symbol);
            assertEquals(symbol, actual.getSymbol());

            if (list.size() == 1) {
                assertEquals(list.get(0).getRule(), actual.getRule());
            } else if (1 < list.size()) {
                // If there are multiple rules for a single key, make sure they are combined
                // into a ChoiceRule.
                var actualRule = assertInstanceOf(ChoiceRule.class, actual.getRule());
                assertEquals(Rule.Kind.CHOICE, actualRule.getKind());
                var exptectedChoices = list.stream()
                        .map(e -> e.getRule())
                        .toList();
                assertIterableEquals(exptectedChoices, actualRule.getChoices());
            } else {
                throw new AssertionError();
            }
        });
    }

    @Test
    @DisplayName("scope()")
    default void scope() throws Exception {
        var target = createTarget();

        var expectedString = expectedProductionSet().stream()
                .map(e -> e.getSymbol() + ":" + e.getRule())
                .sorted()
                .collect(Collectors.joining(",", "{", "}"));
        var actualString = target.scope()
                .map(e -> e.getSymbol() + ":" + e.getRule())
                .sorted()
                .collect(Collectors.joining(",", "{", "}"));
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("add(pr:Production)")
    default void addPr() throws Exception {
        var target = createTarget();

        assertThrows(UnsupportedOperationException.class, () -> target.add(mock(Production.class)));
    }

    @Test
    @DisplayName("remove(ob:Object)")
    default void removeOb() throws Exception {
        var target = createTarget();
        var production = target.iterator().next();

        assertThrows(UnsupportedOperationException.class, () -> target.remove(production));
    }

}
