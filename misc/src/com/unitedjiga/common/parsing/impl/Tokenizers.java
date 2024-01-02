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
package com.unitedjiga.common.parsing.impl;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import com.unitedjiga.common.parsing.Parser;
import com.unitedjiga.common.parsing.Expression;
import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.TokenizerFactory;

public final class Tokenizers {

    private Tokenizers() {
    }

//    public static Token createToken(String s) {
//        String value = String.valueOf(s);
//        return new Token() {
//            @Override
//            public String getValue() {
//                return value;
//            }
//            @Override
//            public String toString() {
//                return value;
//            }
//			@Override
//			public String getName() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//        };
//    }
//
//    public static Tokenizer createTokenizer(Iterator<String> it) {
//        Objects.requireNonNull(it, Message.REQUIRE_NON_NULL.format());
//        return new DefaultTokenizer(it);
//    }

//    static Tokenizer createTokenizer(Reader r) {
//        Objects.requireNonNull(r, Message.REQUIRE_NON_NULL.format());
//        PushbackReader pr = new PushbackReader(r);
//        return new DefaultTokenizer(new Iterator<String>() {
//            @Override
//            public boolean hasNext() {
//                return peek() != -1;
//            }
//
//            @Override
//            public String next() {
//                int ch = read();
//                if (ch == -1) {
//                    throw new NoSuchElementException(Message.NO_SUCH_ELEMENT.format());
//                }
//                return Character.toString(ch);
//            }
//
//            private int read() {
//                try {
//                    return pr.read();
//                } catch (IOException e) {
//                    throw new UncheckedIOException(e);
//                }
//            }
//
//            private void unread(int ch) {
//                assert ch != -1;
//                try {
//                    pr.unread(ch);
//                } catch (IOException e) {
//                    throw new UncheckedIOException(e);
//                }
//            }
//
//            private int peek() {
//                int ch = read();
//                if (ch != -1) {
//                    unread(ch);
//                }
//                return ch;
//            }
//
//            @Override
//            public String toString() {
//                return "Next: " + peek();
//            }
//
//        });
//    }

    public static TokenizerFactory createFactory(Expression... productions) {
        return createFactory(Arrays.asList(productions));
//        Objects.requireNonNull(productions, Message.REQUIRE_NON_NULL.format());
//        return new TokenizerFactory() {
//            
//            @Override
//            public Tokenizer createTokenizer(Reader r) {
//                Tokenizer tzer = Tokenizers.createTokenizer(r);
//                for (Production p : productions) {
////                    Parser pser = p.parser(tzer);
////                    tzer = Tokenizers.createTokenizer(pser.iterativeParse()
////                            .map(s -> s.asToken().getValue())
////                            .iterator());
//                }
//                return tzer;
//            }
//        };
    }
    public static TokenizerFactory createFactory(List<? extends Expression> productions) {
        return new DefaultTokenizerFactory(productions);
    }
    
    public static TokenizerFactory loadFactory(String factoryName, ClassLoader cl) {
        return ServiceLoader.load(TokenizerFactory.class, cl).stream()
                .filter(p -> p.type().getCanonicalName().equals(factoryName))
                .map(Provider::get)
                .findFirst()
                .get();
    }
}
