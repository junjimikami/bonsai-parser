package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.TestCase;

/**
 * 
 * @author Junji Mikami
 */
interface ParserTestCase extends TestCase {

    @Override
    Parser createTarget();

    Tree expectedTree();

    @Test
    @DisplayName("parse() [Closed input stream]")
    default void parseInCaseOfClosedInputStream(TestReporter testReporter) throws Exception {
        var target = createTarget();
        target.close();

        var ex = assertThrows(IllegalStateException.class, () -> target.parse());
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("parse() [Post-parse operation]")
    default void parseInCaseOfPostParseOperation(TestReporter testReporter) throws Exception {
        assumeFalse(expectedTree().getSubTrees().isEmpty());

        var target = createTarget();
        target.parse();

        var ex = assertThrows(IllegalStateException.class, () -> target.parse());
        testReporter.publishEntry(ex.getMessage());
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
