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
package com.unitedjiga.common.parsing.impl;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 *
 * @author Junji Mikami
 */
class OptProduction extends AbstractProduction {
	private final AbstractProduction element;
	private final Pattern pattern;
	
	OptProduction(AbstractProduction p) {
		element = p;
    	pattern =  Pattern.compile("(" + element + ")?");
	}

	@Override
    Symbol interpret(Tokenizer tokenizer, Set<TermProduction> followSet) {
        if (anyMatch(getFirstSet(), tokenizer)) {
        	Symbol symbol = element.interpret(tokenizer, followSet);
        	return newSingleton(this, Optional.of(symbol));
        }
    	return newSingleton(this, Optional.empty());
//        if (anyMatch(followSet, tokenizer)) {
//        	return newSingleton(this, Optional.empty());
//        }
//        Object[] args = {getFirstSet(followSet), tokenizer.hasNext() ? tokenizer.peek() : "EOF"};
//        throw new ParsingException(Messages.RULE_MISMATCH.format(args));
////        throw new ParsingException(Messages.RULE_MISMATCH.format(getFirstSet(followSet), tokenizer.peek()));
    }

    @Override
    Set<TermProduction> getFirstSet(Set<TermProduction> followSet) {
    	return element.getFirstSet(followSet);
    }
    
    @Override
    boolean isOption() {
    	return true;
    }

    @Override
    public Pattern asPattern() {
    	return pattern;
    }
}
