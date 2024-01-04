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
package com.unitedjiga.common.parsing.grammar.impl;

import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.grammar.ChoiceExpression;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.PatternExpression;
import com.unitedjiga.common.parsing.grammar.ReferenceExpression;
import com.unitedjiga.common.parsing.grammar.SequenceExpression;

/**
 * 
 * @author Junji Mikami
 *
 */
public final class GrammarService {
    private static final DefaultGrammarProvider provider = new DefaultGrammarProvider();

    private GrammarService() {
    }

    public static Grammar.Builder createGrammarBuilder() {
        return provider.createGrammarBuilder();
    }

    public static PatternExpression.Builder createPatternBuilder(String regex) {
        return provider.createPatternBuilder(regex);
    }

    public static PatternExpression.Builder createPatternBuilder(Pattern pattern) {
        return provider.createPatternBuilder(pattern);
    }

    public static SequenceExpression.Builder createSequenceBuilder() {
        return provider.createSequenceBuilder();
    }

    public static ChoiceExpression.Builder createChoiceBuilder() {
        return provider.createChoiceBuilder();
    }

    public static ReferenceExpression.Builder createReferenceBuilder(String reference) {
        return provider.createReferenceBuilder(reference);
    }

}
