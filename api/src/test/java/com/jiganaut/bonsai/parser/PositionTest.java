package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

/**
 *
 * @author Junji Mikami
 */
class PositionTest {

    @Test
    @DisplayName("UNKNOWN")
    void unknown(TestReporter testReporter) throws Exception {
        assertNotNull(Position.UNKNOWN);
        testReporter.publishEntry("Position.UNKNOWN: %s".formatted(Position.UNKNOWN));
    }

    @Test
    @DisplayName("of(offset:long) [offset < 0]")
    void ofOffsetWhenOffsetIsNegative(TestReporter testReporter) throws Exception {
        var ex = assertThrows(IllegalArgumentException.class, () -> Position.of(-1));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("of(offset:long)")
    void ofOffset(TestReporter testReporter) throws Exception {
        var actual = Position.of(1);

        assertEquals(1L, actual.offset());
        assertFalse(actual.hasRange());
        testReporter.publishEntry("Position.of(1): %s".formatted(actual));
    }

    @Test
    @DisplayName("withRangeEnd(offset:long)")
    void withRangeEndOffset() throws Exception {
        var actual = Position.of(1).withRangeEnd(3);

        assertTrue(actual.hasRange());
        assertEquals(3L, actual.rangeEnd().offset());
    }

    @Test
    @DisplayName("of(offset:long, line:long, column:long)")
    void ofLineColumn(TestReporter testReporter) throws Exception {
        var actual = Position.of(1, 2, 3);

        assertEquals(1L, actual.offset());
        assertEquals(2L, actual.line());
        assertEquals(3L, actual.column());
        testReporter.publishEntry("Position.of(1, 2, 3): %s".formatted(actual));
    }

    @Test
    @DisplayName("withRangeEnd(offset:long, line:long, column:long)")
    void withRangeEndLineColumn() throws Exception {
        var actual = Position.of(1, 2, 3).withRangeEnd(4, 2, 6);

        assertTrue(actual.hasRange());
        assertEquals(4L, actual.rangeEnd().offset());
        assertEquals(2L, actual.rangeEnd().line());
        assertEquals(6L, actual.rangeEnd().column());
    }

    @Test
    @DisplayName("rangeEndOf(position:Position)")
    void rangeEndOf() throws Exception {
        var position = Position.of(1, 2, 3).withRangeEnd(4, 5, 6);
        var actual = (Position.LineColumn) Position.rangeEndOf(position);

        assertEquals(4L, actual.offset());
        assertEquals(5L, actual.line());
        assertEquals(6L, actual.column());
    }

    @Test
    @DisplayName("rangeOf(start:Position, end:Position)")
    void rangeOf() throws Exception {
        assertEquals(
                Position.of(1, 2, 3).withRangeEnd(4, 5, 6),
                Position.rangeOf(Position.of(1, 2, 3), Position.of(4, 5, 6)));
        assertEquals(
                Position.of(1).withRangeEnd(4),
                Position.rangeOf(Position.of(1), Position.of(4)));
    }

    @Test
    @DisplayName("collapse(position:Position)")
    void collapse() throws Exception {
        assertEquals(
                Position.of(1, 2, 3),
                Position.collapse(Position.of(1, 2, 3).withRangeEnd(4, 5, 6)));
        assertEquals(
                Position.of(1),
                Position.collapse(Position.of(1).withRangeEnd(4)));
        assertEquals(
                Position.UNKNOWN,
                Position.collapse(Position.UNKNOWN));
    }

}