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
package com.unitedjiga.commontest.parsing;

import static org.junit.jupiter.api.Assertions.*;
import static com.unitedjiga.common.parsing.Production.*;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.TokenizerFactory;

/**
 * @author Junji Mikami
 *
 */
public class TokenizerFactoryTest {

    @Test
    void test01() throws Exception {
        var tf = TokenizerFactory.newFactory();
        var tzer = tf.createTokenizer(new StringReader("abc"));
        
        assertTrue(tzer.hasNext());
        assertEquals("a", tzer.next().getValue());
        assertEquals("b", tzer.next().getValue());
        assertEquals("c", tzer.next().getValue());
        assertFalse(tzer.hasNext());
    }
    @Test
    void test02() throws Exception {
        var prd = Production.oneOf(
                of("[0-9]").repeat(),
                of("[a-z]").repeat()
                );
        var tf = TokenizerFactory.newFactory(prd);
        var tzer = tf.createTokenizer(new StringReader("abc123def456"));
        
        assertTrue(tzer.hasNext());
        assertEquals("abc", tzer.next().getValue());
        assertEquals("123", tzer.next().getValue());
        assertEquals("def", tzer.next().getValue());
        assertEquals("456", tzer.next().getValue());
        assertFalse(tzer.hasNext());
    }
    class JavaCommentParsing01 {
        Production InputElement() {
            return Production.oneOf(
                    WhiteSpace(),
                    Comment()
                    );
        }
        Production WhiteSpace() {
            return oneOf("\\x20", "\\t", "\\f", LineTerminator());
        }
        Production LineTerminator() {
            return oneOf("\\n", of("\\r", of("\\n").opt()));
        }

        Production Comment() {
            return of("/", oneOf(TraditionalComment(), EndOfLineComment()));
        }
        Production TraditionalComment() {
            return of("\\*", CommentTail());
        }
        Production CommentTail() {
            return oneOf(of("\\*", CommentTailStar()),
                    of(NotStar(), ref(this::CommentTail)));
        }
        Production CommentTailStar() {
            return oneOf("/",
                    of("\\*", ref(this::CommentTailStar)),
                    of(NotStarNotSlash(), ref(this::CommentTail)));
        }
        Production NotStar() {
            return oneOf("[^*]", LineTerminator());
        }
        Production NotStarNotSlash() {
            return oneOf("[^*/]", LineTerminator());
        }
        Production EndOfLineComment() {
            return of("/", of(".").repeat());
        }
    }
    @Test
    void testJavaCommentParsing01() throws Exception {
        var prd = new JavaCommentParsing01().InputElement();
        var tf = TokenizerFactory.newFactory(prd);
        var tzer = tf.createTokenizer(new StringReader(""
                + " "
                + "// Single line comment\n"
                + "/*\n"
                + " * Multi line comment\n"
                + " */\n"
                + ""));
        
        tzer.forEachRemaining(t -> System.out.println("[" + t + "]"));
    }
    class JavaCommentParsing02 extends JavaCommentParsing01 {
        Production CommentStart() {
            return oneOf(of("/", oneOf("/", "\\*").opt()), "(?s:.)");
        }

        @Override
        Production Comment() {
            return oneOf(TraditionalComment(), EndOfLineComment());
        }
        @Override
        Production TraditionalComment() {
            return of("/\\*", CommentTail());
        }
        @Override
        Production EndOfLineComment() {
            return of("//", of(".").repeat());
        }
    }
    @Test
    void testJavaCommentParsing02() throws Exception {
        var prd0 = new JavaCommentParsing02().CommentStart();
        var prd = new JavaCommentParsing02().InputElement();
        var tf = TokenizerFactory.newFactory(prd0, prd);
        var tzer = tf.createTokenizer(new StringReader(""
                + " "
                + "// Single line comment\n"
                + "/*\n"
                + " * Multi line comment\n"
                + " */\n"
                + ""));
        
        tzer.forEachRemaining(t -> System.out.println("[" + t + "]"));
    }

    @Test
    void testService() throws Exception {
        var tf = TokenizerFactory.loadFactory("com.unitedjiga.commontest.parsing.sp.TestTokenizerFactory", null);
        var tzer = tf.createTokenizer(new StringReader("0110001111"));
        
        assertTrue(tzer.hasNext());
        assertEquals("0", tzer.next().getValue());
        assertEquals("11", tzer.next().getValue());
        assertEquals("000", tzer.next().getValue());
        assertEquals("1111", tzer.next().getValue());
        assertFalse(tzer.hasNext());

    }
}
