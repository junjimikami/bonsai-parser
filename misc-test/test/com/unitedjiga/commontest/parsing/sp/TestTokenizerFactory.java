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
package com.unitedjiga.commontest.parsing.sp;

import java.io.Reader;

import com.unitedjiga.common.parsing.Expression;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.Tokenizer.Builder;
import com.unitedjiga.common.parsing.TokenizerFactory;

/**
 * @author Junji Mikami
 *
 */
public class TestTokenizerFactory implements TokenizerFactory {

    @Override
    public Tokenizer createTokenizer(Reader r) {
        var prd = Expression.oneOf(
                Expression.of("0").repeat(),
                Expression.of("1").repeat());
        return TokenizerFactory.newFactory(prd).createTokenizer(r);
    }

    @Override
    public Builder createTokenizerBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

}
