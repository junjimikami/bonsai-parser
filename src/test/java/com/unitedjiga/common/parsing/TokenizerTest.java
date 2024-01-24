package com.unitedjiga.common.parsing;

import static com.unitedjiga.common.parsing.grammar.Expressions.choice;
import static com.unitedjiga.common.parsing.grammar.Expressions.pattern;
import static com.unitedjiga.common.parsing.grammar.Expressions.sequence;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.Grammar;

class TokenizerTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Nested
    class MonkeyTest {
        @Test
        void test() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0"))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("0"));
            assertTrue(tokenizer.hasNext());
            assertTrue(tokenizer.hasNext("0"));
            assertFalse(tokenizer.hasNext("1"));
            var token = tokenizer.next();
            assertTrue(token.getKind().isTerminal());
            assertEquals("0", token.getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test2() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0|1"))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("0110"));
            assertEquals("0", tokenizer.next().getValue());
            assertEquals("1", tokenizer.next().getValue());
            assertEquals("1", tokenizer.next().getValue());
            assertEquals("0", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test3() {
            var grammar = Grammar.builder()
                    .add("A", choice()
                            .add(pattern("0"))
                            .add(pattern("1")))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("0110"));
            assertEquals("0", tokenizer.next().getValue());
            assertEquals("1", tokenizer.next().getValue());
            assertEquals("1", tokenizer.next().getValue());
            assertEquals("0", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test4() {
            var grammar = Grammar.builder()
                    .add("A", choice()
                            .add("B")
                            .add("C"))
                    .add("B", pattern("0"))
                    .add("C", pattern("1"))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("0110"));
            assertEquals("0", tokenizer.next().getValue());
            assertEquals("1", tokenizer.next().getValue());
            assertEquals("1", tokenizer.next().getValue());
            assertEquals("0", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test5() {
            var grammar = Grammar.builder()
                    .add("A", sequence()
                            .add(pattern("0"))
                            .add(pattern("1")))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("0101"));
            assertEquals("01", tokenizer.next().getValue());
            assertEquals("01", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test6() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .atLeast(2))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("0"));
            assertThrows(ParsingException.class, () -> tokenizer.hasNext());
        }

        @Test
        void test7() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .atLeast(2))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("00"));
            assertEquals("00", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test8() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .atLeast(2))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("000"));
            assertEquals("000", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test9() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .exactly(2))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("0"));
            assertThrows(ParsingException.class, () -> tokenizer.hasNext())
                    .printStackTrace();
        }

        @Test
        void test10() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .exactly(2))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("00"));
            assertEquals("00", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test11() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .exactly(2))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("000"));
            assertEquals("00", tokenizer.next().getValue());
            assertThrows(ParsingException.class, () -> tokenizer.hasNext())
                    .printStackTrace();
        }

        @Test
        void test12() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .range(2, 3))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("0"));
            assertThrows(ParsingException.class, () -> tokenizer.hasNext())
                    .printStackTrace();
        }

        @Test
        void test13() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .range(2, 3))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("00"));
            assertEquals("00", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test14() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .range(2, 3))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("000"));
            assertEquals("000", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test15() {
            var grammar = Grammar.builder()
                    .add("A", pattern("0")
                            .range(2, 3))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("0000"));
            assertEquals("000", tokenizer.next().getValue());
            assertThrows(ParsingException.class, () -> tokenizer.hasNext())
                    .printStackTrace();
        }

        @Test
        void test16() {
            var grammar = Grammar.builder()
                    .add("A", sequence()
                            .add("B")
                            .add("C"))
                    .add("B", pattern("0").opt())
                    .add("C", pattern("1"))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("101"));
            assertEquals("1", tokenizer.next().getValue());
            assertEquals("01", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }

        @Test
        void test17() {
            var grammar = Grammar.builder()
                    .setSkipPattern("\\s")
                    .add("A", sequence()
                            .add(pattern("1"))
                            .add(choice()
                                    .add(pattern("0"))
                                    .addEmpty()))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("10"));
            assertTrue(tokenizer.hasNext());
            assertEquals("10", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }
        
        @Test
        void test18() throws Exception {
            var grammar = Grammar.builder()
                    .add("A", sequence()
                            .add(pattern("1"))
                            .add(set -> Expression.EMPTY))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("1"));
            assertTrue(tokenizer.hasNext());
            assertEquals("1", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }
    }

}
