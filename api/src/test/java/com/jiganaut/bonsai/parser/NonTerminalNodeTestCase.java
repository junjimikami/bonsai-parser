package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.parser.Tree.Kind;

interface NonTerminalNodeTestCase extends TreeTestCase {

    interface BuilderTestCase extends TreeTestCase.BuilderTestCase {

        NonTerminalNode.Builder createTarget();

        NonTerminalNode expectedTree();

        boolean isSetName();

        @Override
        default boolean canBuild() {
            return isSetName();
        }

        @Test
        @DisplayName("setName(String) [Null parameter]")
        default void setNameInCaseOfNullParameter() throws Exception {
            assumeFalse(isSetName());

            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.setName(null));
        }

        @Test
        @DisplayName("build() [setName not excuted]")
        default void buildInCaseOfSetNameNotExcuted() throws Exception {
            assumeFalse(isSetName());

            var builder = createTarget();

            assertThrows(NullPointerException.class, () -> builder.build());
        }

    }

    @Override
    NonTerminalNode createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.NON_TERMINAL;
    }

}
