/*
 * The MIT License
 *
 * Copyright 2022 Mikami Junji.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.unitedjiga.common.parsing.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.PatternProduction;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.ProductionFactory;
import com.unitedjiga.common.parsing.ProductionVisitor;
import com.unitedjiga.common.parsing.QuantifiedProduction;
import com.unitedjiga.common.parsing.Reference;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.TerminalProduction;

/**
 * @author Mikami Junji
 *
 */
class DefaultProductionFactory implements ProductionFactory {
    private Map<String, Production> productions = new HashMap<>();
    private ProductionVisitor<Void, Void> scanner = new ProductionVisitor<>() {

        @Override
        public Void visitAlternative(AlternativeProduction prd, Void p) {
            if (prd.getName() != null) {
                productions.put(prd.getName(), prd);
            }
            prd.getProductions().forEach(this::visit);
            return null;
        }

        @Override
        public Void visitSequential(SequentialProduction prd, Void p) {
            if (prd.getName() != null) {
                productions.put(prd.getName(), prd);
            }
            prd.getProductions().forEach(this::visit);
            return null;
        }

        @Override
        public Void visitPattern(PatternProduction prd, Void p) {
            if (prd.getName() != null) {
                productions.put(prd.getName(), prd);
            }
            return null;
        }

        @Override
        public Void visitReference(Reference<?> prd, Void p) {
            if (prd.getName() != null) {
                productions.put(prd.getName(), prd);
            }
            return null;
        }

        @Override
        public Void visitQuantified(QuantifiedProduction prd, Void p) {
            if (prd.getName() != null) {
                productions.put(prd.getName(), prd);
            }
            visit(prd.get());
            return null;
        }

        @Override
        public Void visitEmpty(TerminalProduction prd, Void p) {
            return null;
        }
    };

    @Override
    public PatternProduction createPattern(String regex) {
        return new TermProduction(regex);
    }

    @Override
    public PatternProduction createPattern(String regex, int flags) {
        return new TermProduction(regex, flags);
    }

    @Override
    public PatternProduction createPattern(String name, String regex) {
        var p = new TermProduction(name, regex);
        productions.put(name, p);
        return p;
    }

    @Override
    public PatternProduction createPattern(String name, String regex, int flags) {
        var p = new TermProduction(name, regex, flags);
        productions.put(name, p);
        return p;
    }

    @Override
    public AlternativeProduction.Builder createAlternativeBuilder() {
        var p = new AltProduction.Builder() {
            @Override
            public AlternativeProduction build() {
                var p = super.build();
                scanner.visit(p);
                return p;
            }
        };
        return p;
    }

    @Override
    public AlternativeProduction.Builder createAlternativeBuilder(String name) {
        var p = new AltProduction.Builder(name) {
            @Override
            public AlternativeProduction build() {
                var p = super.build();
                scanner.visit(p);
                return p;
            }
        };
        return p;
    }

    @Override
    public SequentialProduction.Builder createSequentialBuilder() {
        var p = new SeqProduction.Builder() {
            @Override
            public SequentialProduction build() {
                var p = super.build();
                scanner.visit(p);
                return p;
            }
        };
        return p;
    }

    @Override
    public SequentialProduction.Builder createSequentialBuilder(String name) {
        var p = new SeqProduction.Builder(name) {
            @Override
            public SequentialProduction build() {
                var p = super.build();
                scanner.visit(p);
                return p;
            }
        };
        return p;
    }

    @Override
    public <T extends Production> Reference<T> createReference(Supplier<T> supplier) {
        return new RefProduction<>(supplier);
    }

    @Override
    public <T extends Production> Reference<T> createReference(String name, Supplier<T> supplier) {
        var p = new RefProduction<>(name, supplier);
        productions.put(name, p);
        return p;
    }

    @Override
    public Reference<Production> createReference(String src) {
        return new RefProduction<>(() -> productions.get(src));
    }

    @Override
    public Reference<Production> createReference(String name, String src) {
        var p = new RefProduction<>(name, () -> productions.get(src));
        productions.put(name, p);
        return p;
    }

    @Override
    public Map<String, Production> getProductions() {
        return productions;
    }

}
