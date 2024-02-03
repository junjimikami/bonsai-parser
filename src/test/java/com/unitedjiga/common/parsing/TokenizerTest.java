package com.unitedjiga.common.parsing;

import static com.unitedjiga.common.parsing.grammar.Rules.choiceBuilder;
import static com.unitedjiga.common.parsing.grammar.Rules.pattern;
import static com.unitedjiga.common.parsing.grammar.Rules.sequenceBuilder;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.unitedjiga.common.parsing.grammar.Rule;
import com.unitedjiga.common.parsing.grammar.Grammar;

class TokenizerTest {

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
                    .add("A", choiceBuilder()
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
                    .add("A", choiceBuilder()
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
                    .add("A", sequenceBuilder()
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
            assertThrows(ParseException.class, () -> tokenizer.hasNext());
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
            assertThrows(ParseException.class, () -> tokenizer.hasNext())
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
            assertThrows(ParseException.class, () -> tokenizer.next())
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
            assertThrows(ParseException.class, () -> tokenizer.hasNext())
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
            assertThrows(ParseException.class, () -> tokenizer.next())
                    .printStackTrace();
        }

        @Test
        void test16() {
            var grammar = Grammar.builder()
                    .add("A", sequenceBuilder()
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
                    .add("A", sequenceBuilder()
                            .add(pattern("1"))
                            .add(choiceBuilder()
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
                    .add("A", sequenceBuilder()
                            .add(pattern("1"))
                            .add(set -> Rule.EMPTY))
                    .build();
            var factory = TokenizerFactory.newFactory(grammar);
            var tokenizer = factory.createTokenizer(new StringReader("1"));
            assertTrue(tokenizer.hasNext());
            assertEquals("1", tokenizer.next().getValue());
            assertFalse(tokenizer.hasNext());
        }
    }

    static Stream<Arguments> allMethods() {
        return Stream.of(
                arguments(named("hasNext()", (Consumer<Tokenizer>) t -> t.hasNext())),
                arguments(named("hasNext(String)", (Consumer<Tokenizer>) t -> t.hasNext(""))),
                arguments(named("hasNext(Pattern)", (Consumer<Tokenizer>) t -> t.hasNext(Pattern.compile("")))),
                arguments(named("next()", (Consumer<Tokenizer>) t -> t.next())),
                arguments(named("next(String)", (Consumer<Tokenizer>) t -> t.next(""))),
                arguments(named("next(Pattern)", (Consumer<Tokenizer>) t -> t.next(Pattern.compile("")))),
                arguments(named("skip(String)", (Consumer<Tokenizer>) t -> t.skip(""))),
                arguments(named("skip(Pattern)", (Consumer<Tokenizer>) t -> t.skip(Pattern.compile("")))));
    }

    @DisplayName("[Stream closed]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void streamClosed(Consumer<Tokenizer> method) throws Exception {
        var factory = TokenizerFactory.newFactory(Stubs.DUMMY_GRAMMAR);
        var tokenizer = factory.createTokenizer(Stubs.closedReader());

        assertThrows(UncheckedIOException.class, () -> method.accept(tokenizer))
                .printStackTrace();
    }

    @DisplayName("[Ambiguous rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void ambiguousRule(Consumer<Tokenizer> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", choiceBuilder()
                        .add("A")
                        .add("B"))
                .add("A", pattern("1"))
                .add("B", pattern("."))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(tokenizer))
                .printStackTrace();
    }

    @DisplayName("[Occurrence count out of range]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void occurrenceCountOutOfRange(Consumer<Tokenizer> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1").range(3, 5))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("11"));

        assertThrows(ParseException.class, () -> method.accept(tokenizer))
                .printStackTrace();
    }

    @DisplayName("[Token not match pattern rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void tokenNotMatchPatternRule(Consumer<Tokenizer> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("0"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(tokenizer))
                .printStackTrace();
    }

    @DisplayName("[Token not match choice rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void tokenNotMatchChoiceRule(Consumer<Tokenizer> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", choiceBuilder()
                        .add(pattern("0")))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(tokenizer))
                .printStackTrace();
    }

    @DisplayName("[Token not match sequence rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void tokenNotMatchSequenceRule(Consumer<Tokenizer> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", sequenceBuilder()
                        .add(pattern("0")))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(tokenizer))
                .printStackTrace();
    }

    @DisplayName("[Token not match empty rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void tokenNotMatchEmptyRule(Consumer<Tokenizer> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", set -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(tokenizer))
                .printStackTrace();
    }

    @Test
    @DisplayName("hasNext(st:String) [Null parameter]")
    void hasNextStInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.hasNext((String) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("hasNext(pa:Pattern) [Null parameter]")
    void hasNextPaInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.hasNext((Pattern) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("next(st:String) [Null parameter]")
    void nextStInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.next((String) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("next(pa:Pattern) [Null parameter]")
    void nextPaInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.next((Pattern) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("skip(st:String) [Null parameter]")
    void skipStInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.skip((String) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("skip(pa:Pattern) [Null parameter]")
    void skipPaInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.skip((Pattern) null))
                .printStackTrace();
    }

    @Test
    @DisplayName("next(st:String) [No tokens matching pattern]")
    void nextStInCaseNoTokensMatchingPattern() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NoSuchElementException.class, () -> tokenizer.next("2"))
                .printStackTrace();
    }

    @Test
    @DisplayName("next(pa:Pattern) [No tokens matching pattern]")
    void nextPaInCaseNoTokensMatchingPattern() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("2");

        assertThrows(NoSuchElementException.class, () -> tokenizer.next(pattern))
                .printStackTrace();
    }

    @Test
    @DisplayName("hasNext() [Token remaining]")
    void hasNextInCaseTokenRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertTrue(tokenizer.hasNext());
    }

    @Test
    @DisplayName("hasNext(st:String) [Token remaining]")
    void hasNextStInCaseTokenRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertTrue(tokenizer.hasNext("1"));
    }

    @Test
    @DisplayName("hasNext(pa:Pattern) [Token remaining]")
    void hasNextPaInCaseTokenRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("1");

        assertTrue(tokenizer.hasNext(pattern));
    }

    @Test
    @DisplayName("hasNext() [No tokens remaining]")
    void hasNextInCaseNoTokensRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", set -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());

        assertFalse(tokenizer.hasNext());
    }

    @Test
    @DisplayName("hasNext(st:String) [No tokens remaining]")
    void hasNextStInCaseNoTokensRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", set -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());

        assertFalse(tokenizer.hasNext(""));
    }

    @Test
    @DisplayName("hasNext(pa:Pattern) [No tokens remaining]")
    void hasNextPaInCaseNoTokensRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", set -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());
        var pattern = Pattern.compile("");

        assertFalse(tokenizer.hasNext(pattern));
    }

    @Test
    @DisplayName("next() [Token remaining]")
    void nextInCaseTokenRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertEquals("1", tokenizer.next().getValue());
    }

    @Test
    @DisplayName("next(st:String) [Token remaining]")
    void nextStInCaseTokenRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertEquals("1", tokenizer.next("1").getValue());
    }

    @Test
    @DisplayName("next(pa:Pattern) [Token remaining]")
    void nextPaInCaseTokenRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("1");

        assertEquals("1", tokenizer.next(pattern).getValue());
    }

    @Test
    @DisplayName("next() [No tokens remaining]")
    void nextInCaseNoTokensRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", set -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());

        assertThrows(NoSuchElementException.class, () -> tokenizer.next())
                .printStackTrace();
    }

    @Test
    @DisplayName("next(st:String) [No tokens remaining]")
    void nextStInCaseNoTokensRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", set -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());

        assertThrows(NoSuchElementException.class, () -> tokenizer.next(""))
                .printStackTrace();
    }

    @Test
    @DisplayName("next(pa:Pattern) [No tokens remaining]")
    void nextPaInCaseNoTokensRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", set -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());
        var pattern = Pattern.compile("");

        assertThrows(NoSuchElementException.class, () -> tokenizer.next(pattern))
                .printStackTrace();
    }

    @Test
    @DisplayName("skip(st:String) [Token remaining]")
    void skipStInCaseTokenRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertFalse(tokenizer.skip("1").hasNext());
    }

    @Test
    @DisplayName("skip(pa:Pattern) [Token remaining]")
    void skipPaInCaseTokenRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("1");

        assertFalse(tokenizer.skip(pattern).hasNext());
    }

    @Test
    @DisplayName("skip(st:String) [No tokens remaining]")
    void skipStInCaseNoTokensRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NoSuchElementException.class, () -> tokenizer.skip("2"))
                .printStackTrace();
    }

    @Test
    @DisplayName("skip(pa:Pattern) [No tokens remaining]")
    void skipPaInCaseNoTokensRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("2");

        assertThrows(NoSuchElementException.class, () -> tokenizer.skip(pattern))
                .printStackTrace();
    }

    @Test
    @DisplayName("hasNext()")
    void hasNext() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertTrue(tokenizer.hasNext());
    }

    @Test
    @DisplayName("hasNext(st:String)")
    void hasNextSt() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertTrue(tokenizer.hasNext("1"));
    }

    @Test
    @DisplayName("hasNext(pa:Pattern)")
    void hasNextPa() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("1");

        assertTrue(tokenizer.hasNext(pattern));
    }

    @Test
    @DisplayName("next()")
    void next() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var token = tokenizer.next();

        assertEquals(Tree.Kind.TERMINAL, token.getKind());
        assertEquals("1", token.getValue());
    }

    @Test
    @DisplayName("next(st:String)")
    void nextSt() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var token = tokenizer.next("1");

        assertEquals(Tree.Kind.TERMINAL, token.getKind());
        assertEquals("1", token.getValue());

    }

    @Test
    @DisplayName("next(pa:Pattern)")
    void nextPa() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("1");
        var token = tokenizer.next(pattern);

        assertEquals(Tree.Kind.TERMINAL, token.getKind());
        assertEquals("1", token.getValue());
    }

    @Test
    @DisplayName("skip(st:String)")
    void skipSt() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("0|1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("01"));
        tokenizer.skip("0");
        var token = tokenizer.next();

        assertEquals(Tree.Kind.TERMINAL, token.getKind());
        assertEquals("1", token.getValue());
    }

    @Test
    @DisplayName("skip(pa:Pattern)")
    void skipPa() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("0|1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("01"));
        var pattern = Pattern.compile("0");
        tokenizer.skip(pattern);
        var token = tokenizer.next();

        assertEquals(Tree.Kind.TERMINAL, token.getKind());
        assertEquals("1", token.getValue());
    }
}
