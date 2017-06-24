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
package com.unitedjiga.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.Deque;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Junji Mikami
 */
public class LexerTest {

    public LexerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Lexerを使用した簡単な四則演算サンプルです。
     */
    @Test
    public void sample() {
        System.out.println("---- sample ----");

        /**
         * 演算子の優先順位
         */
        int[] opPrior = new int[0xff];
        opPrior['+'] = 1;
        opPrior['-'] = 1;
        opPrior['*'] = 2;
        opPrior['/'] = 2;

        String expr1 = "1 + 2 * 3 - 4 / 5";
        System.out.println("Input: " + expr1);

        StringBuilder expr2 = new StringBuilder();

        /*
         * step1.中置記法を逆ポーランド記法へ変換
         */
        try (Lexer lexer = new Lexer(new StringReader(expr1))) {
            lexer.useWhitespace(' ').useWord('0', '9');

            Deque<Character> stack = new LinkedList<>();
            while (lexer.hasNext()) {
                if (lexer.hasNextWord()) {
                    expr2.append(lexer.nextWord()).append(' ');
                } else {
                    char ch2 = lexer.nextChar();
                    for (char ch1 : stack) {
                        if (opPrior[ch1] < opPrior[ch2]) {
                            break;
                        }
                        expr2.append(stack.pop()).append(' ');
                    }
                    stack.push(ch2);
                }
            }
            stack.stream().forEach(ch -> expr2.append(ch).append(' '));
            System.out.println("RPN: " + expr2);

        } catch (Exception ex) {
            fail(ex.getMessage());
            return;
        }

        /**
         * 四則演算
         */
        BiFunction<Double, Double, Double>[] opFunc = new BiFunction[0xff];
        opFunc['+'] = (d1, d2) -> d1 + d2;
        opFunc['-'] = (d1, d2) -> d1 - d2;
        opFunc['*'] = (d1, d2) -> d1 * d2;
        opFunc['/'] = (d1, d2) -> d1 / d2;

        /*
         * step2.逆ポーランド記法の数式を計算
         */
        try (Lexer lexer = new Lexer(new StringReader(expr2.toString()))) {
            lexer.useWhitespace(' ').useWord('0', '9');

            Deque<Double> stack = new LinkedList<>();
            while (lexer.hasNext()) {
                if (lexer.hasNextWord()) {
                    stack.push(Double.valueOf(lexer.nextWord()));
                } else {
                    char op = lexer.nextChar();
                    double d2 = stack.pop();
                    double d1 = stack.pop();
                    stack.push(opFunc[op].apply(d1, d2));
                }
            }
            assertEquals(1, stack.size());
            System.out.println("Answer: " + stack.pop());

        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void test1() {
        Lexer lexer = new Lexer(null);
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void test2_1() {
        Lexer lexer = new Lexer(new StringReader(""));
        assertFalse(lexer.hasNext());
        lexer.next();
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void test2_2() {
        Lexer lexer = new Lexer(new StringReader(""));
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void test2_3() {
        Lexer lexer = new Lexer(new StringReader(""));
        assertFalse(lexer.hasNextChar());
        lexer.nextChar();
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void test2_4() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.peek();
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void test2_5() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.peekWord();
        fail();
    }

    @Test(expected = NoSuchElementException.class)
    public void test2_6() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.peekChar();
        fail();
    }

    @Test
    public void test2_7() {
        Lexer lexer = new Lexer(new StringReader(""));
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test3_1() {
        Lexer lexer = new Lexer(new StringReader("0"));
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test3_2() {
        Lexer lexer = new Lexer(new StringReader("0"));
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test3_3() {
        Lexer lexer = new Lexer(new StringReader("0"));
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test3_4() {
        Lexer lexer = new Lexer(new StringReader("0"));
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test3_5() {
        Lexer lexer = new Lexer(new StringReader("0"));
        lexer.peekWord();
        fail();
    }

    @Test
    public void test3_6() {
        Lexer lexer = new Lexer(new StringReader("0"));
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    @Test
    public void test4_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test4_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test4_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test4_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test4_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.peekWord();
        fail();
    }

    @Test
    public void test4_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    // The char value never becomes negative.
//    @Test(expected = IndexOutOfBoundsException.class)
    public void test5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace((char) -1);
        fail();
    }

    @Test
    public void test6_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0');
        assertTrue(lexer.hasNext());
        assertEquals("1", lexer.next());
        assertEquals("0", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test6_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0');
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test6_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0');
        assertTrue(lexer.hasNextChar());
        assertEquals('1', lexer.nextChar());
        assertEquals("0", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test6_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0');
        assertEquals("1", lexer.peek());
        assertEquals("1", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test6_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0');
        lexer.peekWord();
        fail();
    }

    @Test
    public void test6_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0');
        assertEquals('1', lexer.peekChar());
        assertEquals('1', lexer.peekChar());
    }

    @Test
    public void test7_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset('0');
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test7_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset('0');
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test7_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset('0');
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test7_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset('0');
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test7_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset('0');
        lexer.peekWord();
        fail();
    }

    @Test
    public void test7_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset('0');
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    @Test
    public void test8_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset();
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test8_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset();
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test8_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset();
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test8_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset();
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test8_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset();
        lexer.peekWord();
        fail();
    }

    @Test
    public void test8_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').reset();
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    @Test
    public void test9_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').useWhitespace('0');
        assertTrue(lexer.hasNext());
        assertEquals("1", lexer.next());
        assertEquals("0", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test9_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').useWhitespace('0');
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test9_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').useWhitespace('0');
        assertTrue(lexer.hasNextChar());
        assertEquals('1', lexer.nextChar());
        assertEquals("0", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test9_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').useWhitespace('0');
        assertEquals("1", lexer.peek());
        assertEquals("1", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test9_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').useWhitespace('0');
        lexer.peekWord();
        fail();
    }

    @Test
    public void test9_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').useWhitespace('0');
        assertEquals('1', lexer.peekChar());
        assertEquals('1', lexer.peekChar());
    }

    @Test
    public void test10_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('9').useWord('0', '8');
        assertTrue(lexer.hasNext());
        assertEquals("012345678", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("9", lexer.trailingWhitespace());
    }

    @Test
    public void test10_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('9').useWord('0', '8');
        assertTrue(lexer.hasNextWord());
        assertEquals("012345678", lexer.nextWord());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("9", lexer.trailingWhitespace());
    }

    @Test(expected = NoSuchElementException.class)
    public void test10_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('9').useWord('0', '8');
        assertFalse(lexer.hasNextChar());
        lexer.nextChar();
        fail();
    }

    @Test
    public void test10_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('9').useWord('0', '8');
        assertEquals("012345678", lexer.peek());
        assertEquals("012345678", lexer.peek());
    }

    @Test
    public void test10_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('9').useWord('0', '8');
        assertEquals("012345678", lexer.peekWord());
        assertEquals("012345678", lexer.peekWord());
    }

    @Test(expected = InputMismatchException.class)
    public void test10_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('9').useWord('0', '8');
        lexer.peekChar();
        fail();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test11() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('8', '0');
        fail();
    }

    @Test
    public void test12_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8');
        assertTrue(lexer.hasNext());
        assertEquals("9", lexer.next());
        assertEquals("012345678", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test12_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8');
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test12_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8');
        assertTrue(lexer.hasNextChar());
        assertEquals('9', lexer.nextChar());
        assertEquals("012345678", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test12_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8');
        assertEquals("9", lexer.peek());
        assertEquals("9", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test12_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8');
        lexer.peekWord();
        fail();
    }

    @Test
    public void test12_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8');
        assertEquals('9', lexer.peekChar());
        assertEquals('9', lexer.peekChar());
    }

    @Test
    public void test13_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset('0', '8');
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test13_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset('0', '8');
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test13_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset('0', '8');
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test13_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset('0', '8');
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test13_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset('0', '8');
        lexer.peekWord();
        fail();
    }

    @Test
    public void test13_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset('0', '8');
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    @Test
    public void test14_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset();
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test14_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset();
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test14_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset();
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test14_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset();
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test14_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset();
        lexer.peekWord();
        fail();
    }

    @Test
    public void test14_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').reset();
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    @Test
    public void test15_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').useWhitespace('0', '8');
        assertTrue(lexer.hasNext());
        assertEquals("9", lexer.next());
        assertEquals("012345678", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test15_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').useWhitespace('0', '8');
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test15_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').useWhitespace('0', '8');
        assertTrue(lexer.hasNextChar());
        assertEquals('9', lexer.nextChar());
        assertEquals("012345678", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test15_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').useWhitespace('0', '8');
        assertEquals("9", lexer.peek());
        assertEquals("9", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test15_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').useWhitespace('0', '8');
        lexer.peekWord();
        fail();
    }

    @Test
    public void test15_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').useWhitespace('0', '8');
        assertEquals('9', lexer.peekChar());
        assertEquals('9', lexer.peekChar());
    }

    @Test
    public void test16_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('1', '9');
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("123456789", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test16_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('1', '9');
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test16_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('1', '9');
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("123456789", lexer.trailingWhitespace());
    }

    @Test
    public void test16_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('1', '9');
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test16_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('1', '9');
        lexer.peekWord();
        fail();
    }

    @Test
    public void test16_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('1', '9');
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    // The char value never becomes negative.
//    @Test(expected = IndexOutOfBoundsException.class)
    public void test17() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord((char) -1);
        fail();
    }

    @Test
    public void test18_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0');
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test18_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0');
        assertTrue(lexer.hasNextWord());
        assertEquals("0", lexer.nextWord());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test18_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0');
        assertFalse(lexer.hasNextChar());
        lexer.nextChar();
        fail();
    }

    @Test
    public void test18_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0');
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test
    public void test18_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0');
        assertEquals("0", lexer.peekWord());
        assertEquals("0", lexer.peekWord());
    }

    @Test(expected = InputMismatchException.class)
    public void test18_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0');
        lexer.peekChar();
        fail();
    }

    @Test
    public void test19_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset('0');
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test19_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset('0');
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test19_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset('0');
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test19_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset('0');
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test19_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset('0');
        lexer.peekWord();
        fail();
    }

    @Test
    public void test19_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset('0');
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    @Test
    public void test20_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset();
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test20_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset();
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test20_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset();
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test20_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset();
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test20_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset();
        lexer.peekWord();
        fail();
    }

    @Test
    public void test20_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0').reset();
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    @Test
    public void test21_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').useWord('0');
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test21_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').useWord('0');
        assertTrue(lexer.hasNextWord());
        assertEquals("0", lexer.nextWord());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test21_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').useWord('0');
        assertFalse(lexer.hasNextChar());
        lexer.nextChar();
        fail();
    }

    @Test
    public void test21_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').useWord('0');
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test
    public void test21_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').useWord('0');
        assertEquals("0", lexer.peekWord());
        assertEquals("0", lexer.peekWord());
    }

    @Test(expected = InputMismatchException.class)
    public void test21_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0').useWord('0');
        lexer.peekChar();
        fail();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test22() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('8', '0');
        fail();
    }

    @Test
    public void test23_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8');
        assertTrue(lexer.hasNext());
        assertEquals("012345678", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test23_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8');
        assertTrue(lexer.hasNextWord());
        assertEquals("012345678", lexer.nextWord());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test23_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8');
        assertFalse(lexer.hasNextChar());
        lexer.nextChar();
        fail();
    }

    @Test
    public void test23_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8');
        assertEquals("012345678", lexer.peek());
        assertEquals("012345678", lexer.peek());
    }

    @Test
    public void test23_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8');
        assertEquals("012345678", lexer.peekWord());
        assertEquals("012345678", lexer.peekWord());
    }

    @Test(expected = InputMismatchException.class)
    public void test23_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8');
        lexer.peekChar();
        fail();
    }

    @Test
    public void test24_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset('0', '8');
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test24_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset('0', '8');
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test24_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset('0', '8');
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test24_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset('0', '8');
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test24_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset('0', '8');
        lexer.peekWord();
        fail();
    }

    @Test
    public void test24_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset('0', '8');
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    @Test
    public void test25_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset();
        assertTrue(lexer.hasNext());
        assertEquals("0", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test25_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset();
        assertFalse(lexer.hasNextWord());
        lexer.nextWord();
        fail();
    }

    @Test
    public void test25_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset();
        assertTrue(lexer.hasNextChar());
        assertEquals('0', lexer.nextChar());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test25_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset();
        assertEquals("0", lexer.peek());
        assertEquals("0", lexer.peek());
    }

    @Test(expected = InputMismatchException.class)
    public void test25_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset();
        lexer.peekWord();
        fail();
    }

    @Test
    public void test25_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWord('0', '8').reset();
        assertEquals('0', lexer.peekChar());
        assertEquals('0', lexer.peekChar());
    }

    @Test
    public void test26_1() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').useWord('0', '8');
        assertTrue(lexer.hasNext());
        assertEquals("012345678", lexer.next());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test
    public void test26_2() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').useWord('0', '8');
        assertTrue(lexer.hasNextWord());
        assertEquals("012345678", lexer.nextWord());
        assertEquals("", lexer.skippedWhitespace());
        assertEquals("", lexer.trailingWhitespace());
    }

    @Test(expected = InputMismatchException.class)
    public void test26_3() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').useWord('0', '8');
        assertFalse(lexer.hasNextChar());
        lexer.nextChar();
        fail();
    }

    @Test
    public void test26_4() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').useWord('0', '8');
        assertEquals("012345678", lexer.peek());
        assertEquals("012345678", lexer.peek());
    }

    @Test
    public void test26_5() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').useWord('0', '8');
        assertEquals("012345678", lexer.peekWord());
        assertEquals("012345678", lexer.peekWord());
    }

    @Test(expected = InputMismatchException.class)
    public void test26_6() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.useWhitespace('0', '8').useWord('0', '8');
        lexer.peekChar();
        fail();
    }

    // The char value never becomes negative.
//    @Test(expected = IndexOutOfBoundsException.class)
    public void test27() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.reset((char) -1);
        fail();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test28() {
        Lexer lexer = new Lexer(new StringReader("0123456789"));
        lexer.reset('8', '0');
        fail();
    }


    @Test(expected = UncheckedIOException.class)
    public void test29_1() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.hasNext();
        fail();
    }

    @Test(expected = UncheckedIOException.class)
    public void test29_2() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.next();
        fail();
    }

    @Test(expected = UncheckedIOException.class)
    public void test29_3() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.hasNextWord();
        fail();
    }

    @Test(expected = UncheckedIOException.class)
    public void test29_4() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.nextWord();
        fail();
    }

    @Test(expected = UncheckedIOException.class)
    public void test29_5() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.hasNextChar();
        fail();
    }

    @Test(expected = UncheckedIOException.class)
    public void test29_6() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.nextChar();
        fail();
    }

    @Test(expected = UncheckedIOException.class)
    public void test29_7() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.trailingWhitespace();
        fail();
    }

    @Test(expected = UncheckedIOException.class)
    public void test29_8() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.peek();
        fail();
    }

    @Test(expected = UncheckedIOException.class)
    public void test29_9() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.peekWord();
        fail();
    }

    @Test(expected = UncheckedIOException.class)
    public void test29_10() {
        Lexer lexer = new Lexer(new StringReader(""));
        lexer.close();
        lexer.peekChar();
        fail();
    }


    @Test
    public void testXXX1() {
        System.out.println("com.unitedjiga.common.util.LexerTest.testXXX1()");
        String input = IntStream.rangeClosed(0x0000, 0x01ff)
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());
        Lexer lexer = new Lexer(new StringReader(input));
        while (lexer.hasNext()) {
            if (lexer.hasNextWord()) {
                String token = lexer.nextWord();
                System.out.println(token);
            } else {
                lexer.nextChar();
            }
        }
    }

    @Test
    public void testXXX2() {
        System.out.println("com.unitedjiga.common.util.LexerTest.testXXX2()");
        try (Lexer lexer = new Lexer(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Lexer.html"))))) {
            lexer.useWhitespace((char) 0x00, (char) 0x20)
                 .useWord((char) 0x21, (char) 0xff);
            while (lexer.hasNext()) {
                String token = lexer.next();
                System.out.print(lexer.skippedWhitespace());
                System.out.print(token);
            }
            System.out.print(lexer.trailingWhitespace());
        }
    }
}
