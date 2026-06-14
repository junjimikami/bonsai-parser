package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;

/**
 *
 * @author Junji Mikami
 */
class ArrayListRule<T> extends ArrayList<Rule<T>> implements SequenceRule<T> {

    ArrayListRule(SequenceRule<T> sequence) {
        super(sequence.getRules());
    }

    @Override
    public List<Rule<T>> getRules() {
        return this;
    }

}
