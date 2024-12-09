package com.jiganaut.bonsai.parser.impl;

import java.util.Objects;

import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.Token;

/**
 * 
 * @author Junji Mikami
 */
class DefaultToken extends AbstractTree implements Token {

    /**
     * 
     * @author Junji Mikami
     */
    static class Builder extends AbstractTree.Builder implements TerminalNode.Builder {
        @Override
        public DefaultToken.Builder setName(String name) {
            check();
            return (Builder) super.setName(name);
        }

        @Override
        public DefaultToken.Builder setValue(String value) {
            checkParameter(value);
            return (Builder) super.setValue(value);
        }

        @Override
        public DefaultToken build() {
            Objects.requireNonNull(value, Message.VALUE_NOT_SET.format());
            checkForBuild();
            return new DefaultToken(name, value);
        }
    }

    private final String name;
    private final String value;

    /**
     * @param value
     */
    DefaultToken(String name, String value) {
        assert value != null;
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        if (name != null) {
            sb.append("\"");
            sb.append(name);
            sb.append("\"");
            sb.append(":");
        }
        sb.append("\"");
        sb.append(encode(value));
        sb.append("\"");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TerminalNode t) {
            return this.getKind() == t.getKind()
                    && Objects.equals(this.getName(), t.getName())
                    && this.getValue().equals(t.getValue())
                    && this.getSubTrees().equals(t.getSubTrees());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), getName(), getValue(), getSubTrees());
    }

}
