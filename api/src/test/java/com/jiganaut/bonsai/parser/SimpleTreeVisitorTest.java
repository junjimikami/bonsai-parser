package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockErrorNode;
import static com.jiganaut.bonsai.parser.MockFactory.mockNonTerminalNode;
import static com.jiganaut.bonsai.parser.MockFactory.mockToken;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Junji Mikami
 */
class SimpleTreeVisitorTest implements TreeVisitorTestCase<String, String, String> {

    private static final class TestVisitor implements SimpleTreeVisitor<String, String, String> {

        @Override
        public String defaultAction(Tree<String> tree, String p) {
            return "%s:%s".formatted(tree.getKind(), p);
        }

    }

    @Override
    public TreeVisitor<String, String, String> createTarget() {
        return new TestVisitor();
    }

    @Override
    public String createParameter() {
        return "test";
    }

    @Test
    @DisplayName("visitTerminal(te:TerminalNode, p:P)")
    void visitTerminal() throws Exception {
        var visitor = new TestVisitor();

        assertEquals("TERMINAL:test", visitor.visitTerminal(mockToken(), createParameter()));
    }

    @Test
    @DisplayName("visitNonTerminal(nt:NonTerminalNode, p:P)")
    void visitNonTerminal() throws Exception {
        var visitor = new TestVisitor();

        assertEquals("NON_TERMINAL:test", visitor.visitNonTerminal(mockNonTerminalNode(), createParameter()));
    }

    @Test
    @DisplayName("visitError(er:ErrorNode, p:P)")
    void visitError() throws Exception {
        var visitor = new TestVisitor();

        assertEquals("ERROR:test", visitor.visitError(mockErrorNode(), createParameter()));
    }

}