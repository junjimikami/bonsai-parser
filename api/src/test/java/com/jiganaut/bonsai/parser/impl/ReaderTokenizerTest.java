package com.jiganaut.bonsai.parser.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.parser.Position;
import com.jiganaut.bonsai.parser.Token;

class ReaderTokenizerTest {

    private static void assertPosition(
            Token<String> token,
            long startOffset,
            long startLine,
            long startColumn,
            long endOffset,
            long endLine,
            long endColumn) {
        var start = (Position.LineColumn) token.getPosition();
        var end = start.rangeEnd();
        assertEquals(startOffset, start.offset());
        assertEquals(startLine, start.line());
        assertEquals(startColumn, start.column());
        assertEquals(endOffset, end.offset());
        assertEquals(endLine, end.line());
        assertEquals(endColumn, end.column());
    }

    @Test
    @DisplayName("tracks forward movement in UTF-16 code units")
    void tracksForwardMovementInUtf16CodeUnits() throws Exception {
        try (var target = new ReaderTokenizer(new StringReader("a𝒜"))) {
            assertTrue(target.hasNext());
            var token1 = target.next();
            assertEquals("a", token1.getValue());
            assertPosition(token1, 0, 1, 1, 1, 1, 2);

            assertTrue(target.hasNext());
            var token2 = target.next();
            assertEquals("𝒜", token2.getValue());
            assertPosition(token2, 1, 1, 2, 3, 1, 4);

            assertFalse(target.hasNext());
        }
    }

    @Test
    @DisplayName("backspace rolls back previous cursor changes")
    void backspaceRollsBackPreviousCursorChanges() throws Exception {
        try (var target = new ReaderTokenizer(new StringReader("ab\b\b"))) {
            var token1 = target.next();
            assertEquals("a", token1.getValue());
            assertPosition(token1, 0, 1, 1, 1, 1, 2);

            var token2 = target.next();
            assertEquals("b", token2.getValue());
            assertPosition(token2, 1, 1, 2, 2, 1, 3);

            var token3 = target.next();
            assertEquals("\b", token3.getValue());
            assertPosition(token3, 2, 1, 3, 3, 1, 2);

            var token4 = target.next();
            assertEquals("\b", token4.getValue());
            assertPosition(token4, 3, 1, 2, 4, 1, 1);

            assertFalse(target.hasNext());
        }
    }

    @Test
    @DisplayName("carriage return rewinds to line head")
    void carriageReturnRewindsToLineHead() throws Exception {
        try (var target = new ReaderTokenizer(new StringReader("ab\rc"))) {
            var token1 = target.next();
            assertEquals("a", token1.getValue());
            assertPosition(token1, 0, 1, 1, 1, 1, 2);

            var token2 = target.next();
            assertEquals("b", token2.getValue());
            assertPosition(token2, 1, 1, 2, 2, 1, 3);

            var token3 = target.next();
            assertEquals("\r", token3.getValue());
            assertPosition(token3, 2, 1, 3, 3, 1, 1);

            var token4 = target.next();
            assertEquals("c", token4.getValue());
            assertPosition(token4, 3, 1, 1, 4, 1, 2);

            assertFalse(target.hasNext());
        }
    }

    @Test
    @DisplayName("one backspace undoes one CRLF logical change")
    void oneBackspaceUndoesOneCrlfLogicalChange() throws Exception {
        try (var target = new ReaderTokenizer(new StringReader("ab\r\n\bX"))) {
            var token1 = target.next();
            assertEquals("a", token1.getValue());
            assertPosition(token1, 0, 1, 1, 1, 1, 2);

            var token2 = target.next();
            assertEquals("b", token2.getValue());
            assertPosition(token2, 1, 1, 2, 2, 1, 3);

            var token3 = target.next();
            assertEquals("\r", token3.getValue());
            assertPosition(token3, 2, 1, 3, 3, 1, 1);

            var token4 = target.next();
            assertEquals("\n", token4.getValue());
            assertPosition(token4, 3, 1, 1, 4, 2, 1);

            var token5 = target.next();
            assertEquals("\b", token5.getValue());
            assertPosition(token5, 4, 2, 1, 5, 1, 3);

            var token6 = target.next();
            assertEquals("X", token6.getValue());
            assertPosition(token6, 5, 1, 3, 6, 1, 4);

            assertFalse(target.hasNext());
        }
    }
}
