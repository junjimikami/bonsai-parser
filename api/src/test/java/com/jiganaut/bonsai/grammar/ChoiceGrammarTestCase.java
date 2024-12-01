package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Junji Mikami
 *
 */
interface ChoiceGrammarTestCase extends GrammarTestCase {

    interface BuilderTestCase extends GrammarTestCase.BuilderTestCase {

        @Override
        ChoiceGrammar.Builder createTarget();

        @Test
        @DisplayName("hidden() [No elements]")
        default void hiddenInCaseOfNoElements() throws Exception {
            assumeTrue(isNoElements());

            var builder = createTarget();

            assertThrows(IllegalStateException.class, () -> builder.hidden());
        }

        @Test
        @DisplayName("hidden() [Post-build operation]")
        default void hiddenInCaseOfPostBuild() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.hidden());
        }

        @Test
        @DisplayName("hidden()")
        default void hidden() throws Exception {
            assumeTrue(canBuild());

            var target = createTarget();

            assertEquals(target, target.hidden());
        }

        @Test
        @DisplayName("add(st:String, ru:Rule) [Symbol added before hidden]")
        default void addStRuInCaseOfSymbolAddedBeforeHidden() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget().hidden();

            var symbol = expectedProductionSet().stream()
                    .findFirst()
                    .get()
                    .getSymbol();
            assertThrows(IllegalStateException.class, () -> builder.add(symbol, mock(Rule.class)));
        }

        @Test
        @DisplayName("add(st:String, rb:Rule.Builder) [Symbol added before hidden]")
        default void addStRbInCaseOfSymbolAddedBeforeHidden() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget().hidden();

            var symbol = expectedProductionSet().stream()
                    .findFirst()
                    .get()
                    .getSymbol();
            assertThrows(IllegalStateException.class, () -> builder.add(symbol, mock(Rule.Builder.class)));
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
