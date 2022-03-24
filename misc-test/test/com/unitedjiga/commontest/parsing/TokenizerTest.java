package com.unitedjiga.commontest.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.Tokenizer;

class TokenizerTest {

    final String input = "Last night I said these words to my girl.";

    @BeforeEach
    void beforeEach() {
        System.out.println("---------------------------");
    }

//    @Test
//    void test01() {
//        var words = input.split(" ");
//        var tzer = Tokenizer.wrap(words);
//        for (var s : words) {
//            assertTrue(tzer.hasNext());
//            assertEquals(s, tzer.next().getValue());
//        }
//        assertFalse(tzer.hasNext());
//        assertThrows(NoSuchElementException.class, tzer::next).printStackTrace();
//        assertThrows(UnsupportedOperationException.class, tzer::remove).printStackTrace();
//    }
//
//    @Test
//    void test02() {
//        var scanner = new Scanner(input);
//        var tzer = Tokenizer.wrap(scanner);
//        for (var s : input.split(" ")) {
//            assertTrue(tzer.hasNext());
//            assertEquals(s, tzer.next().getValue());
//        }
//        assertFalse(tzer.hasNext());
//        assertThrows(NoSuchElementException.class, tzer::next).printStackTrace();
//        assertThrows(UnsupportedOperationException.class, tzer::remove).printStackTrace();
//    }
//
//    @Test
//    void test03() {
//        var words = input.split(" ");
//        var tzer = Tokenizer.wrap("Last", "night", "I", "said", "these", "words", "to", "my", "girl.");
//        for (var s : words) {
//            assertTrue(tzer.hasNext());
//            assertEquals(s, tzer.next().getValue());
//        }
//        assertFalse(tzer.hasNext());
//        assertThrows(NoSuchElementException.class, tzer::next).printStackTrace();
//        assertThrows(UnsupportedOperationException.class, tzer::remove).printStackTrace();
//    }
//
//    @Test
//    void testNull() {
//        assertThrows(NullPointerException.class, () -> Tokenizer.wrap((String[]) null))
//                .printStackTrace();
//        assertThrows(NullPointerException.class, () -> Tokenizer.wrap((Iterator<CharSequence>) null))
//                .printStackTrace();
//    }
//    
//    @Test
//    void testEmpty() {
//        var tzer = Tokenizer.wrap();
//        assertFalse(tzer.hasNext());
//        var buf = tzer.buffer();
//        assertFalse(buf.hasRemaining());
//    }

    @Test
    void testBuffer() {
        var words = input.split(" ");
        var tzer = Tokenizer.wrap(words);
//        var buf = tzer.buffer();
        var buf = tzer;

        System.out.println(buf.toString());
        assertTrue(buf.hasRemaining());
        assertTrue(buf.isEmpty());
        assertEquals(words[0], buf.get().getValue());
        System.out.println(buf.toString());
        assertTrue(buf.hasRemaining());
        assertFalse(buf.isEmpty());
        assertEquals(words[1], buf.get().getValue());
        System.out.println(buf.toString());
        assertTrue(buf.hasRemaining());
        assertFalse(buf.isEmpty());
        assertEquals(words[2], buf.get().getValue());
        System.out.println(buf.toString());
        buf.reset();
        System.out.println(buf.toString());
        assertTrue(buf.isEmpty());
        assertEquals(words[0], buf.get().getValue());
        System.out.println(buf.toString());
        assertFalse(buf.isEmpty());
        assertEquals(words[1], buf.get().getValue());
        System.out.println(buf.toString());
        buf.pushBack();
        System.out.println(buf.toString());
        assertEquals(words[1], buf.get().getValue());
        buf.pushBack();
        System.out.println(buf.toString());
        buf.pushBack();
        System.out.println(buf.toString());
        assertEquals(words[0], buf.get().getValue());
        assertEquals(words[1], buf.get().getValue());
        assertEquals(words[2], buf.get().getValue());
        System.out.println(buf.toString());

        assertEquals(words[0], buf.remove().getValue());
        System.out.println(buf.toString());
        buf.reset();
        System.out.println(buf.toString());
        assertEquals(words[1], buf.get().getValue());
        System.out.println(buf.toString());
        assertFalse(buf.isEmpty());
        assertEquals(words[1], buf.remove().getValue());
        System.out.println(buf.toString());
        assertTrue(buf.isEmpty());

        var it = Arrays.asList(words).listIterator(2);
        while (buf.hasRemaining()) {
            assertEquals(it.next(), buf.get().getValue());
            System.out.println(buf.toString());
        }
        assertFalse(buf.hasRemaining());
        assertFalse(buf.isEmpty());
//        var it2 = Arrays.asList(words).listIterator(2);
//        buf.tokens().forEach(t->assertEquals(it2.next(), t.getValue()));
        buf.reset();
        assertTrue(buf.hasRemaining());
        assertTrue(buf.isEmpty());
        System.out.println(buf.toString());
    }

    @Test
    void testBuffer02() {
        var words = input.split(" ");
        {
            var tzer = Tokenizer.wrap(words);
//            var buf = tzer.buffer();
            var buf = tzer;
            assertThrows(NoSuchElementException.class, buf::remove)
                    .printStackTrace();
        }
        {
            var tzer = Tokenizer.wrap(words);
//            var buf = tzer.buffer();
            var buf = tzer;
            assertThrows(NoSuchElementException.class, buf::pushBack)
                    .printStackTrace();
        }
        {
            var tzer = Tokenizer.wrap(words);
//            var buf = tzer.buffer();
            var buf = tzer;
            while (buf.hasRemaining()) {
                buf.get();
            }
            assertThrows(NoSuchElementException.class, buf::get)
                    .printStackTrace();
        }
    }

}
