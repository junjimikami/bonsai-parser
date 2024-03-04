package com.jiganaut.bonsai.grammar;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

final class Stubs {

    static final Rule DUMMY_RULE = rule("DUMMY_RULE");

    static final Rule.Builder DUMMY_RULE_BUILDER = builderOf(DUMMY_RULE);

    static final ProductionSet DUMMY_PRODUCTION_SET = new ProductionSet() {

        @Override
        public boolean containsSymbol(String symbol) {
            return true;
        }

        @Override
        public Production get(String symbol) {
            return productionOf(symbol);
        }

        @Override
        public Iterator<Production> iterator() {
            return List.<Production>of().iterator();
        }
    };
    static final ProductionSet EMPTY_PRODUCTION_SET = new ProductionSet() {

        @Override
        public boolean containsSymbol(String symbol) {
            return false;
        }

        @Override
        public Production get(String symbol) {
            throw new NoSuchElementException("TEST CODE");
        }
        
        @Override
        public Iterator<Production> iterator() {
            return List.<Production>of().iterator();
        }
    };

    static Rule rule(String s) {
        return new Rule() {

            @Override
            public Kind getKind() {
                return Kind.PATTERN;
            }

            @Override
            public <R, P> R accept(RuleVisitor<R, P> visitor, P p) {
                throw new AssertionError();
            }

            @Override
            public String toString() {
                return s;
            }
        };
    }

    static Rule.Builder builderOf(Rule rule) {
        return new Rule.Builder() {

            @Override
            public Rule build(ProductionSet set) {
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
