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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.unitedjiga.common.parsing.NonTerminalSymbol;
import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.SingletonSymbol;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 *
 * @author Junji Mikami
 */
abstract class AbstractProduction implements Production {
    
    @Override
    public Symbol parse(Tokenizer tokenizer) {
    	Symbol symbol = interpret(tokenizer, Collections.singleton(TermProduction.EOF));
    	if (tokenizer.hasNext()) {
	        throw new ParsingException(Messages.TOO_MANY_TOKEN.format(tokenizer.peek()));
		}
		return symbol;
    }
    
    @Override
    public Production opt() {
    	return new OptProduction(this);
    }

    @Override
    public Production repeat() {
    	return new RepeatProduction(this);
    }

    @Override
    public String toString() {
    	return asPattern().pattern();
    }

    abstract Set<TermProduction> getFirstSet(Set<TermProduction> followSet);
    
    abstract Symbol interpret(Tokenizer tokenizer, Set<TermProduction> followSet);
    
    abstract boolean isOption();

    static boolean anyMatch(Set<TermProduction> set, Tokenizer tokenizer) {
    	if (!tokenizer.hasNext()) {
			return set.contains(TermProduction.EOF);
		}
    	return set.stream().anyMatch(p -> p.matches(tokenizer.peek()));
    }
    static NonTerminalSymbol newNonTerminal(Production origin, List<Symbol> list) {
    	return new NonTerminalSymbol() {
			
			@Override
			public Iterator<Symbol> iterator() {
				return list.iterator();
			}
			
			@Override
			public Production getOrigin() {
				return origin;
			}

			@Override
			public boolean isEmpty() {
				return list.isEmpty();
			}
			
			@Override
			public String toString() {
				return list.stream().map(Symbol::toString).collect(Collectors.joining());
			}
    	};
    }
    static SingletonSymbol newSingleton(Production origin, Optional<Symbol> symbol) {
    	return new SingletonSymbol() {
			
			@Override
			public Production getOrigin() {
				return origin;
			}
			
			@Override
			public Symbol get() {
				return symbol.get();
			}

			@Override
			public boolean isEmpty() {
				return symbol.isEmpty();
			}

			@Override
			public Iterator<Symbol> iterator() {
				return symbol.stream().iterator();
			}

			@Override
			public String toString() {
				return symbol.stream().map(Symbol::toString).collect(Collectors.joining());
			}
    	};
    }
}
