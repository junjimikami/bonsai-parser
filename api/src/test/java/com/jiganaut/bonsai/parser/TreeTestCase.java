package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockToken;
import static com.jiganaut.bonsai.parser.MockFactory.mockTreeVisitor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jiganaut.bonsai.TestCase;
import com.jiganaut.bonsai.parser.Tree.Kind;

/**
 *
 * @author Junji Mikami
 */
interface TreeTestCase<T> extends TestCase {

    interface BuilderTestCase<T> extends TestCase {

        Tree.Builder<T> createTarget();

        Tree<T> expectedTree();

        boolean canBuild();

        @SuppressWarnings("exports")
        @Test
        @DisplayName("build() [Post-build]")
        default void buildWhenPostBuild(TestReporter testReporter) throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            var ex = assertThrows(IllegalStateException.class, () -> builder.build());
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

        @Test
        @DisplayName("build()")
        default void build() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();

            assertEquals(expectedTree(), builder.build());
        }

    }

    Tree<T> createTarget();

    Kind expectedKind();

    default Position expectedPosition() {
        return Position.UNKNOWN;
    }

    List<Tree<T>> expectedSubTrees();

    List<T> expectedValues();

    @Test
    @DisplayName("equals(Object)")
    default void equals(TestReporter testReporter) throws Exception {
        var target = createTarget();

        assertFalse(target.equals(mockToken()));
        assertTrue(target.equals(createTarget()));
    }

    @Test
    @DisplayName("hashCode()")
    default void hashCode(TestReporter testReporter) throws Exception {
        var target = createTarget();

        testReporter.publishEntry("hashCode()=%s".formatted(String.valueOf(target.hashCode())));
    }

    @Test
    @DisplayName("toString()")
    default void toString(TestReporter testReporter) throws Exception {
        var target = createTarget();

        testReporter.publishEntry("toString()=%s".formatted(target.toString()));
    }

    @Test
    @DisplayName("getKind()")
    default void getKind() throws Exception {
        var target = createTarget();

        assertEquals(expectedKind(), target.getKind());
    }

    @Test
    @DisplayName("getPosition()")
    default void getPosition() throws Exception {
        var target = createTarget();

        assertEquals(expectedPosition(), target.getPosition());
    }

    @Test
    @DisplayName("subTrees()")
    default void subTrees() throws Exception {
        var target = createTarget();

        assertIterableEquals(expectedSubTrees(), target.subTrees().toList());
    }

    @Test
    @DisplayName("values()")
    default void values() throws Exception {
        var target = createTarget();

        assertIterableEquals(expectedValues(), target.values().toList());
    }

    @Test
    @DisplayName("accept(tv:TreeVisitor) [tv == null]")
    default void acceptTvWhenTvIsNull(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> target.accept(null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("accept(tv:TreeVisitor)")
    default void acceptTv() throws Exception {
        var target = createTarget();
        TreeVisitor<T, Void, Void> visitor = mockTreeVisitor();

        target.accept(visitor);

        switch (target) {
            case NonTerminalNode<T> nonTerminal -> verify(visitor).visitNonTerminal(nonTerminal, null);
            case TerminalNode<T> terminal -> verify(visitor).visitTerminal(terminal, null);
            case ErrorNode<T> error -> verify(visitor).visitError(error, null);
        }
    }

    @DisplayName("accept(tv:TreeVisitor, p:P)")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "test" })
    default void acceptTvP(String arg) throws Exception {
        var target = createTarget();
        TreeVisitor<T, Void, String> visitor = mockTreeVisitor();

        target.accept(visitor, arg);

        switch (target) {
            case NonTerminalNode<T> nonTerminal -> verify(visitor).visitNonTerminal(nonTerminal, arg);
            case TerminalNode<T> terminal -> verify(visitor).visitTerminal(terminal, arg);
            case ErrorNode<T> error -> verify(visitor).visitError(error, arg);
        }
    }
}

