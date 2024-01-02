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

import java.util.OptionalLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Mikami Junji
 *
 */
public interface QuantifiedProduction extends EntityProduction {

    public static interface Builder extends EntityProduction.Builder {
        public Builder setName(String name);
        public Builder set(Production p);
        public Builder set(Production.Builder b);
        public default Builder exactly(long times) {
            return range(times, times);
        }
        public Builder atLeast(long times);
        public Builder range(long from, long to);
        public QuantifiedProduction build();
    }

    @Override
    public default Kind getKind() {
        return Kind.QUANTIFIED;
    }

    @Override
    public default <R, P> R accept(ProductionVisitor<R, P> visitor, P p) {
        return visitor.visitQuantified(this, p);
    }

//    public Production get();
    public long getLowerLimit();
    public OptionalLong getUpperLimit();
    public Stream<Production> stream();
    public default boolean withinRange(Predicate<? super Production> predicate) {
        long count = stream()
                .takeWhile(predicate)
                .count();
        return getLowerLimit() <= count &&
                count <= getUpperLimit().orElse(count);
    }
}
