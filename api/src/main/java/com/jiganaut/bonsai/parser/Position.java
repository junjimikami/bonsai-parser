package com.jiganaut.bonsai.parser;

import java.util.List;

import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public sealed interface Position permits Position.Unknown, Position.Range, Position.Point {

    /**
     *
     */
    public static final class Unknown implements Position {

        private Unknown() {
            // no-op
        }

        @Override
        public List<Position.Point> points() {
            return List.of();
        }

        @Override
        public String toString() {
            return "unknown";
        }

    }

    /**
     *
     */
    public static final class Range implements Position {

        private final Position.Point start;
        private final Position.Point end;

        private Range(Position.Point start, Position.Point end) {
            assert start != null;
            assert end != null;
            assert start.getClass() == end.getClass();
            this.start = start;
            this.end = end;
        }

        public Position.Point start() {
            return start;
        }

        public Position.Point end() {
            return end;
        }

        @Override
        public List<Position.Point> points() {
            return List.of(start, end);
        }

        @Override
        public String toString() {
            return String.format("%s..%s", start, end);
        }

    }

    /**
     *
     */
    public static sealed class Point implements Position permits LineColumn {

        private final long offset;

        private Point(long offset) {
            assert 0 <= offset;
            this.offset = offset;
        }

        public long offset() {
            return offset;
        }

        @Override
        public List<Position.Point> points() {
            return List.of(this);
        }

        @Override
        public String toString() {
            return Long.toString(offset);
        }

    }

    /**
     *
     */
    public static final class LineColumn extends Point {

        private final long line;
        private final long column;

        private LineColumn(long offset, long line, long column) {
            super(offset);
            assert 1 <= line;
            assert 1 <= column;
            this.line = line;
            this.column = column;
        }

        public long line() {
            return line;
        }

        public long column() {
            return column;
        }

        @Override
        public String toString() {
            return String.format("%d:%d (offset=%d)", line, column, offset());
        }

    }

    public static final Position.Unknown UNKNOWN = new Unknown();

    public static Position.Point of(long offset) {
        if (offset < 0) {
            throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("offset", 0));
        }
        return new Point(offset);
    }

    public static Position.Range ofRange(long startOffset, long endOffset) {
        return new Range(of(startOffset), of(endOffset));
    }

    public static Position.LineColumn ofLineColumn(long offset, long line, long column) {
        if (offset < 0) {
            throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("offset", 0));
        }
        if (line < 1) {
            throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("line", 1));
        }
        if (column < 1) {
            throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("column", 1));
        }
        return new LineColumn(offset, line, column);
    }

    public static Position.Range ofLineColumnRange(
            long startOffset,
            long startLine,
            long startColumn,
            long endOffset,
            long endLine,
            long endColumn) {
        return new Range(
                ofLineColumn(startOffset, startLine, startColumn),
                ofLineColumn(endOffset, endLine, endColumn));
    }

    public List<Position.Point> points();

    @Override
    public String toString();

}
