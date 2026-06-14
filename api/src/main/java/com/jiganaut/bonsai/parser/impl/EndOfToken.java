package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;

import com.jiganaut.bonsai.parser.Position;
import com.jiganaut.bonsai.parser.Token;

/**
 *
 * @author Junji Mikami
 */
final class EndOfToken<T> implements Token<T> {

    private final Position position;

    EndOfToken(Position position) {
        assert position != null;
        this.position = position;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public T getValue() {
        return null;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("EOF at ");
        sb.append(position);
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EndOfToken<?> other) {
            return this.getKind() == other.getKind()
                    && this.position.equals(other.getPosition());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), position);
    }

}
