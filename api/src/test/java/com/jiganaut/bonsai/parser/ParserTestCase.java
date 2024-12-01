package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.TestCase;

interface ParserTestCase extends TestCase {

    @Override
    Parser createTarget();

    Tree expectedTree();

    @Test
    @DisplayName("parse() [Closed input stream]")
    default void parseInCaseOfClosedInputStream() throws Exception {
        var target = createTarget();
        target.close();

        assertThrows(IllegalStateException.class, () -> target.parse());
    }

    @Test
    @DisplayName("parse() [Post-parse operation]")
    default void parseInCaseOfPostParseOperation() throws Exception {
        assumeFalse(expectedTree().getSubTrees().isEmpty());

        var target = createTarget();
        target.parse();

        assertThrows(IllegalStateException.class, () -> target.parse());
    }

    @Test
    @DisplayName("parse()")
    default void parse() throws Exception {
        var target = createTarget();

        var tree = target.parse();
        assertEquals(expectedTree(), tree);
    }

    @Test
    @DisplayName("close()")
    default void close() throws Exception {
        var target = createTarget();

        assertDoesNotThrow(() -> target.close());
    }

}
