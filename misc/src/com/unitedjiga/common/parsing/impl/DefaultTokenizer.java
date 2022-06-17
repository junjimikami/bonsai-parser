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

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;

/**
 * @author Junji Mikami
 *
 */
class DefaultTokenizer implements Tokenizer {
    static abstract class BaseBuilder implements Tokenizer.Builder {
        @Override
        public Tokenizer.Builder filter(Predicate<Token> p) {
            return new DefaultTokenizer.FilterBuilder(this, p);
        }
    }

    static class DecoratorBuilder extends BaseBuilder {
        private final Tokenizer.Builder builder;
        DecoratorBuilder() {
            this.builder = new DefaultTokenizer.SimpleBuilder();
        }
        DecoratorBuilder(Tokenizer.Builder builder) {
            this.builder = builder;
        }
        @Override
        public Tokenizer.Builder set(Reader r) {
            builder.set(r);
            return this;
        }
        @Override
        public Tokenizer.Builder set(Iterator<String> it) {
            builder.set(it);
            return this;
        }
        @Override
        public Tokenizer build() {
            return builder.build();
        }
    }
    static class FilterBuilder extends DecoratorBuilder {
        private final Predicate<Token> predicate;
        FilterBuilder(Tokenizer.Builder builder, Predicate<Token> predicate) {
            super(builder);
            this.predicate = predicate;
        }
        @Override
        public Tokenizer build() {
            var tzer = super.build();
            return new DefaultTokenizer(new Iterator<Token>() {
                @Override
                public boolean hasNext() {
                    skip();
                    return tzer.hasNext();
                }

                @Override
                public Token next() {
                    skip();
                    return tzer.next();
                }
                
                private void skip() {
                    while (tzer.hasNext()) {
                        if (predicate.test(tzer.peek())) {
                            return;
                        }
                        tzer.read();
                    }
                }
            });
        }
    }
    static class SimpleBuilder extends BaseBuilder {
        private Iterator<Token> it;
        SimpleBuilder() {
        }
        SimpleBuilder(Iterator<Token> it) {
            this.it = it;
        }

        @Override
        public Tokenizer.Builder set(Reader r) {
            var pr = new PushbackReader(r);
            this.it = new Iterator<Token>() {
                @Override
                public boolean hasNext() {
                    return peek() != -1;
                }

                @Override
                public Token next() {
                    int ch = read();
                    if (ch == -1) {
                        throw new NoSuchElementException(Message.NO_SUCH_ELEMENT.format());
                    }
                    return new DefaultToken(Character.toString(ch));
                }

                private int read() {
                    try {
                        return pr.read();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }

                private void unread(int ch) {
                    assert ch != -1;
                    try {
                        pr.unread(ch);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }

                private int peek() {
                    int ch = read();
                    if (ch != -1) {
                        unread(ch);
                    }
                    return ch;
                }
            };
            return this;
        }

        @Override
        public Tokenizer.Builder set(Iterator<String> it) {
            this.it = new Iterator<Token>() {

                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public Token next() {
                    return new DefaultToken(it.next());
                }
                
            };
            return this;
        }

        @Override
        public Tokenizer build() {
            return new DefaultTokenizer(it);
        }
    }

    private final Iterator<Token> itr;
    private final List<Token> buffer = new LinkedList<>();
    private int current = 0;

    DefaultTokenizer(Iterator<Token> itr) {
        this.itr = Objects.requireNonNull(itr);
    }

	@Override
	public Token read() {
        if (buffer.size() <= current) {
            return itr.next();
        }
		return buffer.remove(current);
	}

	@Override
	public boolean hasNext() {
      return current < buffer.size() || itr.hasNext();
	}

	@Override
	public Token next() {
        if (buffer.size() <= current) {
            var t = itr.next();
            buffer.add(t);
        }
        return buffer.get(current++);
	}

	@Override
	public boolean hasPrevious() {
		return 0 < current;
	}

	@Override
	public Token previous() {
		return buffer.get(--current);
	}

}
