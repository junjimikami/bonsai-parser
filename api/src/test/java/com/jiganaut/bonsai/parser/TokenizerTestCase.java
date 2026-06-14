package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.TestCase;

/**
 *
 * @author Junji Mikami
 */
interface TokenizerTestCase<T> extends TestCase {

    @Override
    Tokenizer<T> createTarget();

    Iterator<Token<T>> expectedIterator();

    @Test
    @DisplayName("hasNext()")
    default void hasNext() throws Exception {
        var target = createTarget();

        assertEquals(expectedIterator().hasNext(), target.hasNext());
        assertEquals(expectedIterator().hasNext(), target.hasNext());
    }

    @Test
    @DisplayName("next()")
    default void next(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var expected = expectedIterator();
        while (expected.hasNext()) {
            var next = expected.next();
            var actual = target.next();
            assertEquals(next.getName(), actual.getName());
            assertEquals(next.getValue(), actual.getValue());
        }
        assertFalse(target.hasNext());
        var ex = assertThrows(NoSuchElementException.class, () -> target.next());
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("hasNext() [Post-close]")
    default void hasNextWhenPostClose(TestReporter testReporter)
            throws Exception {
        var target = createTarget();
        target.close();

        var ex = assertThrows(UncheckedIOException.class, () -> target.hasNext());
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("next() [Post-close]")
    default void nextWhenPostClose(TestReporter testReporter)
            throws Exception {
        var target = createTarget();
        target.close();

        var ex = assertThrows(UncheckedIOException.class, () -> target.next());
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("close()")
    default void close() throws Exception {
        var target = createTarget();

        assertDoesNotThrow(() -> target.close());
    }

}

