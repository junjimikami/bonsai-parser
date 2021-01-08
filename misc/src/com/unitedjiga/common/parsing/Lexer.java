/*
 * The MIT License
 *
 * Copyright 2017 Junji Mikami.
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

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_NUMBER;
import static java.io.StreamTokenizer.TT_WORD;
import java.io.UncheckedIOException;
import java.util.BitSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * 文字ストリームを字句解析して文字とワードを読み込むLexer(=Lexical Analyzer)です。
 * 初期設定では文字ストリームをすべて文字トークンに分割します。
 * {@code '\u005Cu0000'}から{@code '\u005Cu00ff'}の範囲の文字をワード構成文字、空白文字に指定できます。
 * 分割されたトークンは反復処理によって取得できます。
 * その際空白文字はスキップされますが取得可能です。
 * 次に例を示します。
 *
 * <pre>{@code
 * try (Lexer lexer = new Lexer(...)) {
 *     lexer.useWhitespace(...)
 *          .useWord(...);
 *     while (lexer.hasNext()) {
 *         String token = lexer.next();
 *         System.out.print(lexer.skippedWhitespace());
 *         System.out.print(token);
 *     }
 *     System.out.print(lexer.trailingWhitespace());
 * }
 * ...
 * }</pre>
 *
 * <p>
 * このクラスはjava.util.Scannerにインスパイアされていますが、入出力エラーの扱いが異なります。
 * 基となる文字ストリームから入出力エラーが発生した場合、非チェック例外であるjava.io.UncheckedIOExceptionにラップしてスローします。
 *
 * @author Junji Mikami
 * @see java.util.Scanner
 */
public final class Lexer implements Iterator<String>, Closeable {

    private final Closeable c;
    private final StreamTokenizer st;
    private final StringBuilder wsBuffer = new StringBuilder();
    private final BitSet wsChars = new BitSet();
    private final BitSet ltChars = new BitSet();
    private String ws = "";

    /**
     * 指定の文字ストリームを字句解析する新しいLexerを構築します。
     *
     * @param r 文字ストリーム
     * @throws NullPointerException 指定の文字ストリームがnullの場合
     */
    public Lexer(Reader r) {
        c = Objects.requireNonNull(r);
        st = new StreamTokenizer(r);
        st.eolIsSignificant(false);
        st.lowerCaseMode(false);
        st.slashSlashComments(false);
        st.slashStarComments(false);
        st.resetSyntax();
    }

    private boolean tryGet(BitSet bit, int c) {
        return c >= 0 && bit.get(c);
    }

    private int nextToken() {
        try {
            st.nextToken();
            while (tryGet(wsChars, st.ttype)) {
                wsBuffer.append((char) st.ttype);
                st.nextToken();
            }
            return st.ttype;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private int peekToken() {
        try {
            return nextToken();
        } finally {
            st.pushBack();
        }
    }

    /**
     * 次のトークンがある場合にtrueを返します。
     *
     * @return 次のトークンがある場合にtrue
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    @Override
    public boolean hasNext() {
        return peekToken() != TT_EOF;
    }

    /**
     * 次のトークンがワードトークンの場合にtrueを返します。
     *
     * @return 次のトークンがワードトークンの場合にtrue
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    public boolean hasNextWord() {
        return peekToken() == TT_WORD;
    }

    /**
     * 次のトークンが文字トークンの場合にtrueを返します。
     *
     * @return 次のトークンが文字トークンの場合にtrueを返します。
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    public boolean hasNextChar() {
        return peekToken() >= 0;
    }

    /**
     * 
     * @return 
     */
    public boolean hasNextInLine() {
        return hasNext() && !tryGet(ltChars, st.ttype);
    }

    /**
     * 
     * @return 
     */
    public boolean hasTrailingWhitespace() {
        return !trailingWhitespace().isEmpty();
    }

    /**
     * 次のトークンを返します。
     *
     * @return 次のトークン
     * @throws NoSuchElementException トークンがこれ以上ない場合
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    @Override
    public String next() {
        switch (nextToken()) {
            case TT_EOF:
                throw new NoSuchElementException();

            case TT_NUMBER:
                throw new AssertionError();

            case TT_WORD:
                ws = wsBuffer.toString();
                wsBuffer.setLength(0);
                return st.sval;

            default:
                ws = wsBuffer.toString();
                wsBuffer.setLength(0);
                return String.valueOf((char) st.ttype);
        }
    }

    /**
     * 次のトークンをワードトークンとして返します。
     *
     * @return 次のワードトークン
     * @throws NoSuchElementException トークンがこれ以上ない場合
     * @throws InputMismatchException 次のトークンがワードトークンではない場合
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    public String nextWord() {
        switch (nextToken()) {
            case TT_EOF:
                throw new NoSuchElementException();

            case TT_NUMBER:
                throw new AssertionError();

            case TT_WORD:
                ws = wsBuffer.toString();
                wsBuffer.setLength(0);
                return st.sval;

            default:
                throw new InputMismatchException();
        }
    }

    /**
     * 次のトークンを文字トークンとして返します。
     *
     * @return 次の文字トークン
     * @throws NoSuchElementException トークンがこれ以上ない場合
     * @throws InputMismatchException 次のトークンが文字トークンではない場合
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    public char nextChar() {
        switch (nextToken()) {
            case TT_EOF:
                throw new NoSuchElementException();

            case TT_NUMBER:
                throw new AssertionError();

            case TT_WORD:
                throw new InputMismatchException();

            default:
                ws = wsBuffer.toString();
                wsBuffer.setLength(0);
                return (char) st.ttype;
        }
    }

    /**
     * 次のトークンを返しますが、反復処理は進みません。
     *
     * @return 次のトークン
     * @throws NoSuchElementException トークンがこれ以上ない場合
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    public String peek() {
        switch (peekToken()) {
            case TT_EOF:
                throw new NoSuchElementException();

            case TT_NUMBER:
                throw new AssertionError();

            case TT_WORD:
                return st.sval;

            default:
                return String.valueOf((char) st.ttype);
        }
    }

    /**
     * 次のトークンをワードトークンとして返しますが、反復処理は進みません。
     *
     * @return 次のワードトークン
     * @throws NoSuchElementException トークンがこれ以上ない場合
     * @throws InputMismatchException 次のトークンがワードトークンではない場合
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    public String peekWord() {
        switch (peekToken()) {
            case TT_EOF:
                throw new NoSuchElementException();

            case TT_NUMBER:
                throw new AssertionError();

            case TT_WORD:
                return st.sval;

            default:
                throw new InputMismatchException();
        }
    }

    /**
     * 次のトークンを文字トークンとして返しますが、反復処理は進みません。
     *
     * @return 次の文字トークン
     * @throws NoSuchElementException トークンがこれ以上ない場合
     * @throws InputMismatchException 次のトークンが文字トークンではない場合
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    public char peekChar() {
        switch (peekToken()) {
            case TT_EOF:
                throw new NoSuchElementException();

            case TT_NUMBER:
                throw new AssertionError();

            case TT_WORD:
                throw new InputMismatchException();

            default:
                return (char) st.ttype;
        }
    }

    /**
     * 現在のトークンの直前にスキップした空白の文字列を返します。
     *
     * @return スキップした空白の文字列。空白をスキップしていない場合、長さ0の文字列。
     */
    public String skippedWhitespace() {
        return ws;
    }

    /**
     * 現在のトークンに後続する空白の文字列を返します。
     *
     * @return 後続する空白の文字列。空白が後続しな場合、長さ0の文字列。
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    public String trailingWhitespace() {
        peekToken();
        return wsBuffer.toString();
    }

    /**
     * 文字設定を初期状態に戻します。
     *
     * @return このLexer
     */
    public Lexer reset() {
        wsChars.clear();
        ltChars.clear();
        st.resetSyntax();
        return this;
    }

    /**
     * 指定の文字を通常文字に戻します。
     *
     * @param ch 設定する文字
     * @return このLexer
     */
    public Lexer reset(char... ch) {
        for (char c : ch) {
            wsChars.clear(c);
            ltChars.clear(c);
            st.ordinaryChar(c);
        }
        return this;
    }

    /**
     * 
     * @param ch
     * @return 
     */
    public Lexer setWordChars(char... ch) {
        reset(ch);
        for (char c : ch) {
            st.wordChars(c, c);
        }
        return this;        
    }

    /**
     * 
     * @param low
     * @param hi
     * @return 
     */
    public Lexer setWordCharRange(char low, char hi) {
        IntStream.rangeClosed(low, hi).forEach(ch -> setWordChars((char) ch));
        return this;
    }

    /**
     * 指定の文字を空白文字に設定します。
     *
     * @param ch 設定する文字
     * @return このLexer
     */
    public Lexer setWhitespaceChars(char... ch) {
        reset(ch);
        for (char c : ch) {
            wsChars.set(c);
        }
        return this;
    }

    /**
     * 
     * @param ch
     * @return 
     */
    public Lexer setLineTerminatorChars(char... ch) {
        reset(ch);
        for (char c : ch) {
            ltChars.set(c);
        }
        return this;
    }
            
    /**
     * 文字ストリームを閉じます。
     *
     * @throws UncheckedIOException 入出力エラーが発生した場合
     */
    @Override
    public void close() {
        try {
            c.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /**
     * 現在のトークンに関する文字列表現を返します。
     *
     * @return 現在のトークンに関する文字列表現
     */
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder("Next: ");
    	sb.append("[");
    	sb.append(trailingWhitespace());
    	sb.append("] ");
    	if (hasNextInLine()) {
    		if (hasNextChar()) {
    	    	sb.append("CHAR'");
    			sb.append(peekChar());
    	    	sb.append("'");
    		} else if (hasNextWord()) {
    	    	sb.append("WORD\"");
    			sb.append(peekWord());
    	    	sb.append("\"");
    		}
		} else if (hasNext()) {
	    	sb.append("EOL[");
			sb.append(peekChar());
	    	sb.append("]");
		} else {
			sb.append("EOF");
		}
        return sb.toString();
    }

}
