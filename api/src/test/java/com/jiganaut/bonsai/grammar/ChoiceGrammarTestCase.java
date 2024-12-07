package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

/**
 * 
 * @author Junji Mikami
 *
 */
interface ChoiceGrammarTestCase extends GrammarTestCase {

    interface BuilderTestCase extends GrammarTestCase.BuilderTestCase {

        @Override
        ChoiceGrammar.Builder createTarget();

        @SuppressWarnings("exports")
        @Test
        @DisplayName("hidden() [No elements]")
        default void hiddenInCaseOfNoElements(TestReporter testReporter) throws Exception {
            assumeTrue(isNoElements());

            var builder = createTarget();

            var ex = assertThrows(IllegalStateException.class, () -> builder.hidden());
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("hidden() [Post-build operation]")
        default void hiddenInCaseOfPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.hidden());
            testReporter.publishEntry(ex.getMessage());
        }

        @Test
        @DisplayName("hidden()")
        default void hidden() throws Exception {
            assumeTrue(canBuild());

            var target = createTarget();

            assertEquals(target, target.hidden());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, ru:Rule) [Symbol added before hidden]")
        default void addStRuInCaseOfSymbolAddedBeforeHidden(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget().hidden();

            var symbol = expectedProductionSet().stream()
                    .findFirst()
                    .get()
                    .getSymbol();
            var ex = assertThrows(IllegalStateException.class, () -> builder.add(symbol, mock(Rule.class)));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(st:String, rb:Rule.Builder) [Symbol added before hidden]")
        default void addStRbInCaseOfSymbolAddedBeforeHidden(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget().hidden();

            var symbol = expectedProductionSet().stream()
                    .findFirst()
                    .get()
                    .getSymbol();
            var ex = assertThrows(IllegalStateException.class, () -> builder.add(symbol, mock(Rule.Builder.class)));
            testReporter.publishEntry(ex.getMessage());
        }

    }

    @Override
    ChoiceGrammar createTarget();

    Set<String> expectedHiddenSymbols();

    @Test
    @DisplayName("getHiddenSymbols()")
    default void getHiddenSymbols() throws Exception {
        var target = createTarget();

        assertEquals(expectedHiddenSymbols(), target.getHiddenSymbols());
    }

}
