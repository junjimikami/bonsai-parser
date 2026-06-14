package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.jiganaut.bonsai.impl.BaseBuilder;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.ErrorNode;
import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.Tree;
import com.jiganaut.bonsai.parser.TreeVisitor;

/**
 *
 * @author Junji Mikami
 *
 */
class DefaultNonTerminalNode<T> implements NonTerminalNode<T> {

    /**
     *
     */
    static class Builder<T> extends BaseBuilder implements NonTerminalNode.Builder<T> {

        private final List<Tree.Builder<T>> builders = new ArrayList<>();
        private final String name;

        Builder(String name) {
            assert name != null;
            this.name = name;
        }

        @Override
        public NonTerminalNode.Builder<T> add(Tree.Builder<T> builder) {
            check();
            Objects.requireNonNull(builder, () -> Message.VALIDATION_PARAMETER_NULL.format("builder"));
            builders.add(builder);
            return this;
        }

        @Override
        public NonTerminalNode.Builder<T> addAll(NonTerminalNode.Builder<T> builder) {
            check();
            Objects.requireNonNull(builder, () -> Message.VALIDATION_PARAMETER_NULL.format("builder"));
            builder.forEach(this::add);
            return this;
        }

        @Override
        public NonTerminalNode<T> build() {
            checkForBuild();
            var list = builders.stream()
                    .map(Tree.Builder::build)
                    .filter(Objects::nonNull)
                    .toList();
            return new DefaultNonTerminalNode<>(name, list);
        }

        @Override
        public Iterator<Tree.Builder<T>> iterator() {
            return Collections.unmodifiableList(builders).iterator();
        }

    }

    private static final TreeVisitor<?, String, String> TO_STRING_VISITOR = new TreeVisitor<>() {

        @Override
        public String visitTerminal(TerminalNode<Object> terminal, String indent) {
            var sb = new StringBuilder();
            sb.append("\n");
            sb.append(indent);
            sb.append("- ");
            if (terminal.getName() != null) {
                sb.append(terminal.getName());
                sb.append(": ");
            }
            sb.append(terminal.getValue());
            return sb.toString();
        }

        @Override
        public String visitNonTerminal(NonTerminalNode<Object> nonTerminal, String indent) {
            var sb = new StringBuilder();
            sb.append("\n");
            sb.append(indent);
            sb.append("- ");
            sb.append(nonTerminal.getName());
            sb.append(":");
            nonTerminal.subTrees().forEach(t -> sb.append(visit(t, indent + "  ")));
            return sb.toString();
        }

        @Override
        public String visitError(ErrorNode<Object> error, String indent) {
            var sb = new StringBuilder();
            sb.append("\n");
            sb.append(indent);
            sb.append("- ");
            sb.append(DefaultErrorNode.toString(error, indent + "  "));
            return sb.toString();
        }

    };

    private final String name;
    private final List<Tree<T>> subTrees;

    private DefaultNonTerminalNode(String name, List<Tree<T>> subTrees) {
        assert name != null;
        assert subTrees != null;
        this.name = name;
        this.subTrees = subTrees;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Tree<T>> getSubTrees() {
        return subTrees;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(name);
        sb.append(":");
        @SuppressWarnings("unchecked")
        var visitor = (TreeVisitor<T, String, String>) TO_STRING_VISITOR;
        subTrees.forEach(t -> sb.append(visitor.visit(t, "  ")));
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NonTerminalNode<?> other) {
            return this.getKind() == other.getKind()
                    && Objects.equals(this.name, other.getName())
                    && this.subTrees.equals(other.getSubTrees());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), name, subTrees);
    }

}
