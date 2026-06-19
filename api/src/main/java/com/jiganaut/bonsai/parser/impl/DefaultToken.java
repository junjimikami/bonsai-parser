package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;

import com.jiganaut.bonsai.parser.Position;
import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.Token;

/**
 *
 * @author Junji Mikami
 */
class DefaultToken<T> implements Token<T> {

    private final String name;
    private final T value;
    private final Position position;

    DefaultToken(String name, T value, Position position) {
        assert value != null;
        assert position != null;
        this.name = name;
        this.value = value;
        this.position = position;
    }

    DefaultToken(String name, T value) {
        this(name, value, Position.UNKNOWN);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        if (name != null) {
            sb.append(name);
            sb.append(": ");
        }
        sb.append(value);
        sb.append(" at ");
        sb.append(position);
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TerminalNode other) {
            return this.getKind() == other.getKind()
                    && Objects.equals(this.name, other.getName())
                    && this.value.equals(other.getValue())
                    && this.position.equals(other.getPosition());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), name, value, position);
    }

}
