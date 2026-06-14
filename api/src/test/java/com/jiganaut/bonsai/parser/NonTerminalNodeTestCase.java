package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockNonTerminalNode;
import static com.jiganaut.bonsai.parser.MockFactory.mockNonTerminalNodeBuilder;
import static com.jiganaut.bonsai.parser.MockFactory.mockToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
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
interface NonTerminalNodeTestCase<T> extends TreeTestCase<T> {

    interface BuilderTestCase<T> extends TreeTestCase.BuilderTestCase<T> {

        NonTerminalNode.Builder<T> createTarget();

        NonTerminalNode<T> expectedTree();

        @Override
        boolean canBuild();

        @Test
        @DisplayName("add(t:Tree)")
        default void add() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();

            assertEquals(builder, builder.add(mockToken()));
        }

        @SuppressWarnings("exports")
        @Test
        @DisplayName("addAll(nb:NonTerminalNode.Builder) [nb == null]")
        default void addAllWhenNbIsNull(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();

            var ex = assertThrows(NullPointerException.class, () -> builder.addAll(null));
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @Test
        @DisplayName("addAll(nb:NonTerminalNode.Builder)")
        default void addAll() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();

            NonTerminalNode<T> mockNode = mockNonTerminalNode();
            when(mockNode.getSubTrees()).thenReturn(List.of());
            NonTerminalNode.Builder<T> mockBuilder = mockNonTerminalNodeBuilder();
            when(mockBuilder.build()).thenReturn(mockNode);
            assertEquals(builder, builder.addAll(mockBuilder));
        }

    }

    @Override
    NonTerminalNode<T> createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.NON_TERMINAL;
    }

    String expectedName();

    @Override
    default List<T> expectedValues() {
        return expectedSubTrees().stream()
                .flatMap(Tree::values)
                .toList();
    }

    @Test
    @DisplayName("getName()")
    default void getName() throws Exception {
        var target = createTarget();

        assertEquals(expectedName(), target.getName());
    }

}

