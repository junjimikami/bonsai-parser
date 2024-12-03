package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.impl.BaseBuilder;
import com.jiganaut.bonsai.parser.Tree;

abstract class AbstractTree implements Tree {

    static abstract class Builder extends BaseBuilder implements Tree.Builder {

        String name;
        String value;

        @Override
        public Tree.Builder setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Tree.Builder setValue(String value) {
            this.value = value;
            return this;
        }
    }

    String encode(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
