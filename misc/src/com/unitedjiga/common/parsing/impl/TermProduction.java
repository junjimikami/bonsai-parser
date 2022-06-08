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

import java.util.Objects;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.PatternProduction;
import com.unitedjiga.common.parsing.Token;

/**
 *
 * @author Junji Mikami
 */
class TermProduction extends AbstractEntityProduction implements PatternProduction {
    static class Builder extends AbstractProduction.Builder implements PatternProduction.Builder {
        private String name;
        private String pattern;
        private int flags;

        @Override
        public Builder setName(String name) {
            check();
            this.name = name;
            return this;
        }

        @Override
        public Builder setPattern(String p) {
            check();
            this.pattern = p;
            return this;
        }

        @Override
        public Builder setFlags(int flags) {
            check();
            this.flags = flags;
            return this;
        }

        @Override
        public PatternProduction build() {
            checkForBuild();
            return new TermProduction(name, pattern, flags);
        }
        
    }

    private final Pattern pattern;

    private TermProduction(String name, String regex, int flags) {
    	super(name);
        Objects.requireNonNull(regex, Message.REQUIRE_NON_NULL.format());
        this.pattern = Pattern.compile(regex, flags);
    }

    public boolean matches(Token t) {
        return pattern.matcher(t.getValue()).matches();
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
