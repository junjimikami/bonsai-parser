package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.Tree;

/**
 * 
 * @author Junji Mikami
 *
 */
class DefaultNonTerminalNode extends AbstractTree implements NonTerminalNode {

    /**
     * 
     * @author Junji Mikami
     */
    static class Builder extends AbstractTree.Builder implements NonTerminalNode.Builder {

        private final List<Tree> list = new ArrayList<>();

        @Override
        public NonTerminalNode.Builder setName(String name) {
            checkParameter(name);
            return (Builder) super.setName(name);
        }

        @Override
        public NonTerminalNode.Builder setValue(String value) {
            check();
            return (Builder) super.setValue(value);
        }

        @Override
        public NonTerminalNode build() {
            Objects.requireNonNull(name, Message.NAME_NOT_SET.format());
            checkForBuild();
            return new DefaultNonTerminalNode(name, value, list);
        }

        @Override
        public NonTerminalNode.Builder add(Tree tree) {
            checkParameter(tree);
            list.add(tree);
            return this;
        }

    }

    private final String name;
    private final String value;
    private final List<? extends Tree> list;

    DefaultNonTerminalNode(String name, String value, List<? extends Tree> list) {
        assert name != null;
        assert list != null;
        this.name = name;
        this.value = value;
        this.list = list;
    }

    DefaultNonTerminalNode(String name, List<? extends Tree> list) {
        this(name, null, list);
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
    public List<? extends Tree> getSubTrees() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(Message.symbolEncode(name));
        if (value != null) {
            sb.append(":");
            sb.append(Message.stringEncode(value));
        }
        if (!list.isEmpty()) {
            sb.append(list.stream()
                    .map(Tree::toString)
                    .collect(Collectors.joining(", ", "(", ")")));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NonTerminalNode nt) {
            return this.getKind() == nt.getKind()
                    && this.getName().equals(nt.getName())
                    && Objects.equals(this.getValue(), nt.getValue())
                    && this.getSubTrees().equals(nt.getSubTrees());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), getName(), getValue(), getSubTrees());
    }

}
