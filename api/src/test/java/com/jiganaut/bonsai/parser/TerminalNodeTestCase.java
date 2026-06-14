package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.parser.Tree.Kind;

/**
 *
 * @author Junji Mikami
 */
interface TerminalNodeTestCase<T> extends TreeTestCase<T> {

    @Override
    TerminalNode<T> createTarget();

    String expectedName();

    T expectedValue();

    @Override
    default Kind expectedKind() {
        return Kind.TERMINAL;
    }

    @Override
    default List<Tree<T>> expectedSubTrees() {
        return List.of();
    }

    @Override
    default List<T> expectedValues() {
        return List.of(expectedValue());
    }

    @Test
    @DisplayName("getName()")
    default void getName() throws Exception {
        var target = createTarget();

        assertEquals(expectedName(), target.getName());
    }

    @Test
    @DisplayName("getValue()")
    default void getValue() throws Exception {
        var target = createTarget();

        assertEquals(expectedValue(), target.getValue());
    }

}
