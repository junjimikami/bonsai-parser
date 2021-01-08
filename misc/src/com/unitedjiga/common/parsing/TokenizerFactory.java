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

import java.io.IOException;

/**
 * 
 * @author Junji Mikami
 *
 */
public interface TokenizerFactory {

	static Tokenizer createTokenizer(Lexer lexer) {
		class LexerToken implements Token {
			private String value;
			private String skippedWhitespace;
			LexerToken(String value, String skippedWhitespace) {
				this.value = value;
				this.skippedWhitespace = skippedWhitespace;
			}

			@Override
			public String getValue() {
				return value;
			}

			@Override
			public String skippedWhitespace() {
				return skippedWhitespace;
			}			
			
			@Override
			public String toString() {
				return skippedWhitespace + value;
			}
		}
		return new Tokenizer() {
			
			@Override
			public void close() throws IOException {
				lexer.close();
			}
			
			@Override
			public Token peek() {
				return new LexerToken(lexer.hasNext() ? lexer.peek() : "", lexer.trailingWhitespace());
			}
			
			@Override
			public Token next() {
				return new LexerToken(lexer.next(), lexer.skippedWhitespace());
			}
			
			@Override
			public boolean hasNext() {
				return lexer.hasNext();
			}
			
			@Override
			public String toString() {
				return lexer.toString();
			}
		};
	}
}
