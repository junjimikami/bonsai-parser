package com.jiganaut.bonsai.parser;

import java.util.Objects;

import com.jiganaut.bonsai.impl.Message;

/**
 * @author Junji Mikami
 *
 */
public sealed interface Position permits Position.Unknown, Position.Offset {

    /**
     *
     */
    public static final class Unknown implements Position {

        private Unknown() {
            // no-op
        }

        @Override
        public String toString() {
            return "unknown";
        }

    }

    /**
     *
     */
    public static sealed class Offset implements Position permits LineColumn {

        private final long offset;
        private final Offset opposite;

        private Offset(long offset) {
            assert 0 <= offset;
            this.offset = offset;
            this.opposite = null;
        }

        private Offset(long offset, Offset opposite) {
            assert 0 <= offset;
            assert opposite != null;
            this.offset = offset;
            this.opposite = opposite;
        }

        private Offset(long offset, long oppositeOffset) {
            assert 0 <= offset;
            assert 0 <= oppositeOffset;
            this.offset = offset;
            this.opposite = new Offset(oppositeOffset, this);
        }

        public long offset() {
            return offset;
        }

        public boolean hasRange() {
            return opposite != null;
        }

        public Offset rangeEnd() {
            return opposite;
        }

        public Offset withRangeEnd(long offset) {
            if (offset < 0) {
                throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("offset", 0));
            }
            return new Offset(this.offset, offset);
        }

        @Override
        public String toString() {
            return Long.toString(offset);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Offset other) {
                return this.offset == other.offset
                        && this.equalsRange(other);
            }
            return super.equals(obj);
        }

        private boolean equalsRange(Offset other) {
            return (this.opposite == null && other.opposite == null)
                    || (this.opposite != null && other.opposite != null
                            && this.opposite.offset == other.opposite.offset);
        }

        @Override
        public int hashCode() {
            return Objects.hash(offset, opposite);
        }

    }

    /**
     *
     */
    public static final class LineColumn extends Offset {

        private final long line;
        private final long column;
        private final LineColumn opposite;

        private LineColumn(long offset, long line, long column) {
            super(offset);
            assert 1 <= line;
            assert 1 <= column;
            this.line = line;
            this.column = column;
            this.opposite = null;
        }

        private LineColumn(long offset, long line, long column, LineColumn opposite) {
            super(offset, opposite);
            assert 1 <= line;
            assert 1 <= column;
            this.line = line;
            this.column = column;
            this.opposite = opposite;
        }

        private LineColumn(long offset, long line, long column, long oppositeOffset, long oppositeLine,
                long oppositeColumn) {
            super(offset);
            assert 1 <= line;
            assert 1 <= column;
            this.line = line;
            this.column = column;
            this.opposite = new LineColumn(oppositeOffset, oppositeLine, oppositeColumn, this);
        }

        public long line() {
            return line;
        }

        public long column() {
            return column;
        }

        @Override
        public boolean hasRange() {
            return opposite != null;
        }

        @Override
        public LineColumn rangeEnd() {
            return opposite;
        }

        public LineColumn withRangeEnd(long offset, long line, long column) {
            if (offset < 0) {
                throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("offset", 0));
            }
            if (line < 1) {
                throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("line", 1));
            }
            if (column < 1) {
                throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("column", 1));
            }
            return new LineColumn(this.offset(), this.line, this.column, offset, line, column);
        }

        @Override
        public String toString() {
            return String.format("[%d,%d](offset=%d)", line, column, offset());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof LineColumn other) {
                return this.offset() == other.offset()
                        && this.line == other.line
                        && this.column == other.column
                        && this.equalsRange(other);
            }
            return super.equals(obj);
        }

        private boolean equalsRange(LineColumn other) {
            return (this.opposite == null && other.opposite == null)
                    || (this.opposite != null && other.opposite != null
                            && this.opposite.offset() == other.opposite.offset()
                            && this.opposite.line == other.opposite.line
                            && this.opposite.column == other.opposite.column);
        }

        @Override
        public int hashCode() {
            return Objects.hash(offset(), line, column, opposite);
        }

    }

    public static final Position.Unknown UNKNOWN = new Unknown();

    public static Position.Offset of(long offset) {
        if (offset < 0) {
            throw new IllegalArgumentException(Message.VALIDATION_PARAMETER_MIN.format("offset", 0));
        }
        return new Offset(offset);
    }

    public static Position.LineColumn of(long offset, long line, long column) {
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

    public static Position rangeEndOf(Position position) {
        if (position instanceof Position.Offset offset) {
            return offset.hasRange() ? offset.rangeEnd() : offset;
        }
        return UNKNOWN;
    }

    public static Position rangeOf(Position start, Position end) {
        if (start instanceof Position.LineColumn startLineColumn) {
            if (end instanceof Position.LineColumn endLineColumn) {
                var rangeEnd = endLineColumn.hasRange() ? endLineColumn.rangeEnd() : endLineColumn;
                return startLineColumn.withRangeEnd(
                        rangeEnd.offset(),
                        rangeEnd.line(),
                        rangeEnd.column());
            }
        }
        if (start instanceof Position.Offset startOffset) {
            if (end instanceof Position.Offset endOffset) {
                var rangeEnd = endOffset.hasRange() ? endOffset.rangeEnd() : endOffset;
                return startOffset.withRangeEnd(rangeEnd.offset());
            }
        }
        return UNKNOWN;
    }

    public static Position collapse(Position position) {
        if (position instanceof Position.LineColumn lineColumn) {
            if (!lineColumn.hasRange()) {
                return lineColumn;
            }
            return new LineColumn(lineColumn.offset(), lineColumn.line(), lineColumn.column());
        }
        if (position instanceof Position.Offset offset) {
            if (!offset.hasRange()) {
                return offset;
            }
            return new Offset(offset.offset());
        }
        return UNKNOWN;
    }

    @Override
    public String toString();

}
