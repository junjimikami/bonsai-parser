package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
