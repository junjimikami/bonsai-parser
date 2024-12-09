package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.parser.Tree.Kind;

/**
 * 
 * @author Junji Mikami
 */
interface NonTerminalNodeTestCase extends TreeTestCase {

    interface BuilderTestCase extends TreeTestCase.BuilderTestCase {

        NonTerminalNode.Builder createTarget();

        NonTerminalNode expectedTree();

        boolean isSetName();

        @Override
        default boolean canBuild() {
            return isSetName();
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("setName(String) [Null parameter]")
        default void setNameInCaseOfNullParameter(TestReporter testReporter) throws Exception {
            assumeFalse(isSetName());

            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.setName(null));
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [setName not excuted]")
        default void buildInCaseOfSetNameNotExcuted(TestReporter testReporter) throws Exception {
            assumeFalse(isSetName());

            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.build());
            testReporter.publishEntry(ex.getMessage());
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("add(Tree) [Null parameter]")
        default void addInCaseOfNullParameter(TestReporter testReporter) throws Exception {
            assumeFalse(isSetName());

            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.add(null));
            testReporter.publishEntry(ex.getMessage());
        }

        @Test
        @DisplayName("add(Tree)")
        default void add() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();

            assertEquals(builder, builder.add(mock(Tree.class)));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("addAll(NonTerminalNode.Builder) [Null parameter]")
        default void addAllInCaseOfNullParameter(TestReporter testReporter) throws Exception {
            assumeFalse(isSetName());

            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.addAll(null));
            testReporter.publishEntry(ex.getMessage());
        }

        @Test
        @DisplayName("addAll(NonTerminalNode.Builder)")
        default void addAll() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();

            var mockNode = mock(NonTerminalNode.class);
            when(mockNode.getSubTrees()).thenReturn(List.of());
            var mockBuilder = mock(NonTerminalNode.Builder.class);
            when(mockBuilder.build()).thenReturn(mockNode);
            assertEquals(builder, builder.addAll(mockBuilder));
        }

    }

    @Override
    NonTerminalNode createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.NON_TERMINAL;
    }

}
