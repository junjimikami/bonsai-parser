package com.jiganaut.bonsai.grammar;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

final class Stubs {

    private static abstract class AbstractProductionSet extends AbstractSet<Production> implements ProductionSet {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Iterator<Production> iterator() {
            return Collections.emptyIterator();
        }
        
    }

    static final Rule DUMMY_RULE = rule("DUMMY_RULE");

    static final Rule.Builder DUMMY_RULE_BUILDER = builderOf(DUMMY_RULE);

    static final ProductionSet DUMMY_PRODUCTION_SET = new AbstractProductionSet() {

        @Override
        public boolean containsSymbol(String symbol) {
            return true;
        }

        @Override
        public Production getProduction(String symbol) {
            return productionOf(symbol);
        }

        @Override
        public Iterator<Production> iterator() {
            return Collections.emptyIterator();
        }
    };
    static final ProductionSet EMPTY_PRODUCTION_SET = new AbstractProductionSet() {

        @Override
        public boolean containsSymbol(String symbol) {
            return false;
        }

        @Override
        public Production getProduction(String symbol) {
            throw new NoSuchElementException("TEST CODE");
        }
        
        @Override
        public Iterator<Production> iterator() {
            return Collections.emptyIterator();
        }
    };

    static Rule rule(String s) {
        return new PatternRule() {

            @Override
            public String toString() {
                return s;
            }

            @Override
            public QuantifierRule atLeast(int times) {
                throw new AssertionError();
            }

            @Override
            public QuantifierRule range(int from, int to) {
                throw new AssertionError();
            }

            @Override
            public SkipRule skip() {
                throw new AssertionError();
            }

            @Override
            public Pattern getPattern() {
                throw new AssertionError();
            }
        };
    }

    static Rule.Builder builderOf(Rule rule) {
        return new Rule.Builder() {

            @Override
            public Rule build() {
                return rule;
            }
        };
    }

    static Production productionOf(String symbol) {
        return new Production() {

            @Override
            public String getSymbol() {
                return symbol;
            }

            @Override
            public Rule getRule() {
                return rule(symbol);
            }
        };
    }
}
