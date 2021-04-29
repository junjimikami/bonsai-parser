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

/**
 * 正規表現ベースの生成規則です。
 * 
 * @author Junji Mikami
 */
public interface Production {

    static SequentialProduction of(Object... args) {
        SequentialProduction.Builder builder = SequentialProduction.builder();
        for (Object o : args) {
            if (o instanceof Production) {
                builder.add((Production) o);
            } else if (o instanceof String) {
                builder.add((String) o);
            } else {
                builder.add(o.toString());
            }
        }
        return builder.build();
    }

    static AlternativeProduction oneOf(Object... args) {
        AlternativeProduction.Builder builder = AlternativeProduction.builder();
        for (Object o : args) {
            if (o instanceof Production) {
                builder.add((Production) o);
            } else if (o instanceof String) {
                builder.add((String) o);
            } else {
                builder.add(o.toString());
            }
        }
        return builder.build();
    }

    Parser parser(Tokenizer tokenizer);

    AlternativeProduction opt();

    SequentialProduction repeat();

    @Override
    String toString();
}
