package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.TestCase;

/**
 *
 * @author Junji Mikami
 */
interface TreeVisitorTestCase<T, R, P> extends TestCase {

    TreeVisitor<T, R, P> createTarget();

    P createParameter();

    @Test
    @DisplayName("visit(tr:Tree) [tr == null]")
    default void visitWhenTrIsNull(TestReporter testReporter) throws Exception {
        var visitor = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> visitor.visit((Tree<T>) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("visit(tr:Tree, p:P) [tr == null]")
    default void visitWhenTrPIsNull(TestReporter testReporter) throws Exception {
        var visitor = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> visitor.visit((Tree<T>) null, createParameter()));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("visit(tr:Tree)")
    default void visit() throws Exception {
        var visitor = createTarget();
        Tree<T> tree = MockFactory.mockToken();

        visitor.visit(tree);

        verify(tree).accept(visitor);
    }

    @Test
    @DisplayName("visit(tr:Tree, p:P)")
    default void visitP() throws Exception {
        var visitor = createTarget();
        Tree<T> tree = MockFactory.mockToken();

        visitor.visit(tree, createParameter());

        verify(tree).accept(visitor, createParameter());
    }

}