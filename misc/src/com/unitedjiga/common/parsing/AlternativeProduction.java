/*
 * The MIT License
 *
 * Copyright 2021 Junji Mikami.
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

/**
 * @author Junji Mikami
 *
 */
public interface AlternativeProduction extends EntityProduction {

    public static interface Builder extends EntityProduction.Builder {
        public Builder setName(String name);
        public Builder add(Production p);
        public Builder add(Production.Builder b);
        public Builder addEmpty();
        public AlternativeProduction build();
    }

    @Override
    public default Kind getKind() {
    	return Kind.ALTERNATIVE;
    }

    @Override
    public default <R, P> R accept(ProductionVisitor<R, P> visitor, P p) {
        return visitor.visitAlternative(this, p);
    }

    public List<? extends Production> getProductions();
}
