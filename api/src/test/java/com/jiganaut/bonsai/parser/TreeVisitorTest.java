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
class TreeVisitorTest implements TreeVisitorTestCase<String, String, String> {

    private static final class TestVisitor implements TreeVisitor<String, String, String> {

        @Override
        public String visitTerminal(TerminalNode<String> terminal, String p) {
            return "TERMINAL:%s".formatted(p);
        }

        @Override
        public String visitNonTerminal(NonTerminalNode<String> nonTerminal, String p) {
            return "NON_TERMINAL:%s".formatted(p);
        }

        @Override
        public String visitError(ErrorNode<String> error, String p) {
            return "ERROR:%s".formatted(p);
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