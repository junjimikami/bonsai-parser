package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.Tree;

/**
 * 
 * @author Junji Mikami
 *
 */
class DefaultNonTerminalNode extends AbstractTree implements NonTerminalNode {

    static class Builder extends AbstractTree.Builder implements NonTerminalNode.Builder {

        private final List<Tree> list = new ArrayList<>();

        @Override
        public NonTerminalNode.Builder setName(String name) {
            checkParameter(name);
            super.setName(name);
            return this;
        }

        @Override
        public NonTerminalNode.Builder setValue(String name) {
            super.setValue(name);
            return this;
        }

        @Override
        public NonTerminalNode build() {
            checkParameter(name);
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
        return list.stream()
                .map(Tree::toString)
                .collect(Collectors.joining());
    }
}
