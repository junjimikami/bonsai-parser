package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.parser.Tree.Kind;

interface TerminalNodeTestCase extends TreeTestCase {

    interface BuilderTestCase extends TreeTestCase.BuilderTestCase {

        TerminalNode.Builder createTarget();

        TerminalNode expectedTree();

        boolean isSetValue();

        @Override
        default boolean canBuild() {
            return isSetValue();
        }

        @Test
        @DisplayName("setValue(String) [Null parameter]")
        default void setValueInCaseOfNullParameter() throws Exception {
            assumeFalse(isSetValue());

            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.setValue(null));
        }

        @Test
        @DisplayName("build() [setValue not excuted]")
        default void buildInCaseOfSetValueNotExcuted() throws Exception {
            assumeFalse(isSetValue());

            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.build());
        }

    }

    @Override
    TerminalNode createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.TERMINAL;
    }

    @Override
    default List<Tree> expectedSubTrees() {
        return List.of();
    }

}
