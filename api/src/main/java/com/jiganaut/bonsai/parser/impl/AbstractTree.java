package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.impl.BaseBuilder;
import com.jiganaut.bonsai.parser.Tree;

/**
 * 
 * @author Junji Mikami
 */
abstract class AbstractTree implements Tree {

    /**
     * 
     * @author Junji Mikami
     */
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

}
