package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.UncheckedIOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.TestCase;

/**
 *
 * @author Junji Mikami
 */
interface ParserTestCase<T> extends TestCase {

    @Override
    Parser<T> createTarget();

    Tree<T> expectedTree();

    @Test
    @DisplayName("parse() [Post-close]")
    default void parseWhenPostClose(TestReporter testReporter) throws Exception {
        var target = createTarget();
        target.close();

        var ex = assertThrows(UncheckedIOException.class, () -> target.parse());
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("parse() [Post-parse]")
    default void parseWhenPostParse(TestReporter testReporter) throws Exception {
        assumeFalse(expectedTree().subTrees().findAny().isEmpty());

        var target = createTarget();
        target.parse();

        var ex = assertThrows(IllegalStateException.class, () -> target.parse());
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
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

