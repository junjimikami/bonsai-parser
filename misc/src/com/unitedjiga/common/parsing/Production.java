/*
 * The MIT License
 *
 * Copyright 2020 Junji Mikami.
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

import java.util.function.Supplier;

import com.unitedjiga.common.parsing.impl.Productions;

/**
 * 正規表現ベースの生成規則です。
 * 
 * @author Junji Mikami
 */
public interface Production {

	public static enum Kind {
		PATTERN,
		SEQUENTIAL,
		ALTERNATIVE,
		REFERENCE;
	}

	public static interface Builder {
		public Production build();
	}

	static SequentialProduction of(Object... args) {
        return Productions.of(args);
    }

    static AlternativeProduction oneOf(Object... args) {
        return Productions.oneOf(args);
    }

    static Production empty() {
        return Productions.empty();
    }

    static Supplier<? extends Production> ref(Supplier<? extends Production> p) {
        return p;
    }

//    Parser parser(Tokenizer tokenizer);

    public <R, P> R accept(ProductionVisitor<R, P> visitor, P p);
    public String getName();
    public Kind getKind();
    
    public Production as(String name);

    public AlternativeProduction opt();

    public SequentialProduction repeat();

    @Override
    String toString();
}
