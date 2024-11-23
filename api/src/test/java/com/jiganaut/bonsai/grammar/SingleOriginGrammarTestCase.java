package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 
 * @author Junji Mikami
 *
 */
interface SingleOriginGrammarTestCase extends GrammarTestCase {

    interface BuilderTestCase extends GrammarTestCase.BuilderTestCase {

        @Override
        SingleOriginGrammar.Builder createTarget();

        String expectedStartSymbol();

        default boolean isMissingStartSymbol() {
            if (expectedStartSymbol() == null) {
                return false;
            }
            return expectedProductionSet().stream()
                    .map(e -> e.getSymbol())
                    .noneMatch(e -> e.equals(expectedStartSymbol()));
        }

        @Override
        default boolean canBuild() {
            return GrammarTestCase.BuilderTestCase.super.canBuild()
                    && !isMissingStartSymbol();
        }

        @Test
        @DisplayName("setStartSymbol(String) [Null parameter]")
        default void setStartSymbolInCaseOfNullParameter() throws Exception {
            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.setStartSymbol(null));
        }

        @Test
        @DisplayName("setStartSymbol(String) [Post-build operation]")
        default void setStartSymbolInCaseOfPostBuild() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.setStartSymbol(""));
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = { "1", "a", "[" })
        @DisplayName("setStartSymbol(String)")
        default void setStartSymbol(String s) throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.setStartSymbol(s));
        }

        @Test
        @DisplayName("build() [Missing start symbol]")
        default void buildInCaseOfMissingStartSymbol() throws Exception {
            assumeTrue(isMissingStartSymbol());

            var builder = createTarget();

            assertThrows(NoSuchElementException.class, () -> builder.build());
        }

    }

    @Override
    SingleOriginGrammar createTarget();

    String expectedStartSymbol();

    @Test
    @DisplayName("getStartProduction()")
    default void getStartProduction() throws Exception {
        var target = createTarget();

        var expected = expectedProductionSet().stream()
                .filter(e -> e.getSymbol().equals(expectedStartSymbol()))
                .findFirst()
                .orElseThrow();
        var actual = target.getStartProduction();
        assertEquals(expected.getSymbol(), actual.getSymbol());
        assertEquals(expected.getRule(), actual.getRule());
    }

    @Test
    @DisplayName("scope()")
    default void scope() throws Exception {
        var target = createTarget();

        var expectedString = expectedProductionSet().stream()
                .filter(e -> e.getSymbol().equals(expectedStartSymbol()))
                .map(e -> e.getSymbol() + ":" + e.getRule())
                .sorted()
                .collect(Collectors.joining(",", "{", "}"));
        var actualString = target.scope()
                .map(e -> e.getSymbol() + ":" + e.getRule())
                .sorted()
                .collect(Collectors.joining(",", "{", "}"));
        assertEquals(expectedString, actualString);
    }

}
