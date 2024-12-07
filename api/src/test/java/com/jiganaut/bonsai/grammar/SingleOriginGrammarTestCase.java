package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
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

        @SuppressWarnings("exports")
        @Test
        @DisplayName("setStartSymbol(String) [Null parameter]")
        default void setStartSymbolInCaseOfNullParameter(TestReporter testReporter) throws Exception {
            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.setStartSymbol(null));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("setStartSymbol(String) [Post-build operation]")
        default void setStartSymbolInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.setStartSymbol(""));
            testReporter.publishEntry(ex.getMessage());
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = { "1", "a", "[" })
        @DisplayName("setStartSymbol(String)")
        default void setStartSymbol(String s) throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.setStartSymbol(s));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Missing start symbol]")
        default void buildInCaseOfMissingStartSymbol(TestReporter testReporter) throws Exception {
            assumeTrue(isMissingStartSymbol());

            var builder = createTarget();

            var ex = assertThrows(NoSuchElementException.class, () -> builder.build());
            testReporter.publishEntry(ex.getMessage());
        }

    }

    @Override
    SingleOriginGrammar createTarget();

    String expectedStartSymbol();

    @Test
    @DisplayName("getStartSymbol()")
    default void getStartSymbol() throws Exception {
        var target = createTarget();

        var expected = expectedProductionSet().stream()
                .filter(e -> e.getSymbol().equals(expectedStartSymbol()))
                .findFirst()
                .orElseThrow();
        var actual = target.getStartSymbol();
        assertEquals(expected.getSymbol(), actual);
    }

}
