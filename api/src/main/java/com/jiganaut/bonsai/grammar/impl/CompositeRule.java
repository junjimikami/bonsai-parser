package com.jiganaut.bonsai.grammar.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.BaseBuilder;

/**
 *
 * @author Junji Mikami
 */
abstract class CompositeRule<T, E extends Collection<Rule<T>>> {

    /**
     *
     */
    static abstract class Builder<T> extends BaseBuilder implements Iterable<Rule.Builder<T>> {
        final Collection<Rule.Builder<T>> builders;

        Builder(Collection<Rule.Builder<T>> builders) {
            assert builders != null;
            this.builders = builders;
        }

        @Override
        public Iterator<Rule.Builder<T>> iterator() {
            return Collections.unmodifiableCollection(builders).iterator();
        }

    }

    final E elements;

    CompositeRule(E elements) {
        assert elements != null;
        this.elements = elements;
    }

}
