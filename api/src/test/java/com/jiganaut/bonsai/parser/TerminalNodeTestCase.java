package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

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

        @SuppressWarnings("exports")
        @Test
        @DisplayName("setValue(String) [Null parameter]")
        default void setValueInCaseOfNullParameter(TestReporter testReporter) throws Exception {
            assumeFalse(isSetValue());

            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.setValue(null));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [setValue not excuted]")
        default void buildInCaseOfSetValueNotExcuted(TestReporter testReporter) throws Exception {
            assumeFalse(isSetValue());

            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.build());
            testReporter.publishEntry(ex.getMessage());
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
