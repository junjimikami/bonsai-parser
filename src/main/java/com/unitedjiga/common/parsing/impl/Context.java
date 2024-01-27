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

import java.util.Set;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.Production;

/**
 * @author Mikami Junji
 *
 */
record Context(Production production, Tokenizer tokenizer, Set<Expression> followSet, Pattern skipPattern) {

    Context withProduction(Production production) {
        return new Context(production, this.tokenizer, this.followSet, this.skipPattern);
    }

    Context withFollowSet(Set<Expression> followSet) {
        return new Context(this.production, this.tokenizer, followSet, this.skipPattern);
    }

    void skip() {
        if (skipPattern == null) {
            return;
        }
        if (!tokenizer.hasNext(skipPattern)) {
            return;
        }
        tokenizer.skip(skipPattern);
    }
    boolean preCheck() {
        var expression = production().getExpression();
        if (!AnyMatcher.scan(expression, this)) {
            skip();
        }
        return tokenizer().hasNext();
    }

    boolean postCheck() {
        skip();
        return tokenizer().hasNext();
    }

}
