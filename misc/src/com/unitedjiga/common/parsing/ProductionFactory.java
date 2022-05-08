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
package com.unitedjiga.common.parsing;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import com.unitedjiga.common.parsing.impl.Productions;

/**
 * @author Mikami Junji
 *
 */
public interface ProductionFactory {

    public static ProductionFactory newFactory() {
        return Productions.newFactory();
    }

    public PatternProduction createPattern(String regex);
    public PatternProduction createPattern(String regex, int flags);
    public PatternProduction createPattern(String name, String regex);
    public PatternProduction createPattern(String name, String regex, int flags);

    public AlternativeProduction.Builder createAlternativeBuilder();
    public AlternativeProduction.Builder createAlternativeBuilder(String name);

    public SequentialProduction.Builder createSequentialBuilder();
    public SequentialProduction.Builder createSequentialBuilder(String name);

    public <T extends Production> Reference<T> createReference(Supplier<T> supplier);
    public <T extends Production> Reference<T> createReference(String name, Supplier<T> supplier);

    public default Reference<Production> createReference(String src) {
        Objects.requireNonNull(src);
        var p = getProductions().stream()
                .filter(e -> src.contentEquals(e.getName()))
                .findFirst();
        return createReference(p::get);
    }
    public default Reference<Production> createReference(String name, String src) {
        Objects.requireNonNull(src);
        var p = getProductions().stream()
                .filter(e -> src.contentEquals(e.getName()))
                .findFirst();
        return createReference(name, p::get);
    }

    public List<Production> getProductions();
}
