package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.grammar.Rules.choiceBuilder;
import static com.jiganaut.bonsai.grammar.Rules.pattern;
import static com.jiganaut.bonsai.grammar.Rules.reference;
import static com.jiganaut.bonsai.grammar.Rules.sequenceBuilder;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Rule;

class TokenizerTest {

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

        assertThrows(UncheckedIOException.class, () -> method.accept(tokenizer));
    }

    @DisplayName("[Ambiguous rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void ambiguousRule(Consumer<Tokenizer> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", choiceBuilder()
                        .add(pattern("1"))
                        .add(pattern(".")))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(tokenizer));
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

        assertThrows(ParseException.class, () -> method.accept(tokenizer));
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

        assertThrows(ParseException.class, () -> method.accept(tokenizer));
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

        assertThrows(ParseException.class, () -> method.accept(tokenizer));
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

        assertThrows(ParseException.class, () -> method.accept(tokenizer));
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

        assertThrows(ParseException.class, () -> method.accept(tokenizer));
    }

    @Test
    @DisplayName("hasNext(st:String) [Null parameter]")
    void hasNextStInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.hasNext((String) null));
    }

    @Test
    @DisplayName("hasNext(pa:Pattern) [Null parameter]")
    void hasNextPaInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.hasNext((Pattern) null));
    }

    @Test
    @DisplayName("next(st:String) [Null parameter]")
    void nextStInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.next((String) null));
    }

    @Test
    @DisplayName("next(pa:Pattern) [Null parameter]")
    void nextPaInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.next((Pattern) null));
    }

    @Test
    @DisplayName("skip(st:String) [Null parameter]")
    void skipStInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.skip((String) null));
    }

    @Test
    @DisplayName("skip(pa:Pattern) [Null parameter]")
    void skipPaInCaseNullParameter() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.skip((Pattern) null));
    }

    @Test
    @DisplayName("next(st:String) [No tokens matching pattern]")
    void nextStInCaseNoTokensMatchingPattern() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NoSuchElementException.class, () -> tokenizer.next("2"));
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

        assertThrows(NoSuchElementException.class, () -> tokenizer.next(pattern));
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

        assertThrows(NoSuchElementException.class, () -> tokenizer.next());
    }

    @Test
    @DisplayName("next(st:String) [No tokens remaining]")
    void nextStInCaseNoTokensRemaining() throws Exception {
        var grammar = Grammar.builder()
                .add("S", set -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());

        assertThrows(NoSuchElementException.class, () -> tokenizer.next(""));
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

        assertThrows(NoSuchElementException.class, () -> tokenizer.next(pattern));
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

        assertThrows(NoSuchElementException.class, () -> tokenizer.skip("2"));
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

        assertThrows(NoSuchElementException.class, () -> tokenizer.skip(pattern));
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
        assertTrue(token.getKind().isTerminal());
        assertFalse(token.getKind().isNonTerminal());
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

    @DisplayName("Test various grammars")
    @ParameterizedTest
    @MethodSource
    void testVariousGrammars(Grammar grammar, String input, List<String> tokens) {
        var factory = TokenizerFactory.newFactory(grammar);
        var tokenizer = factory.createTokenizer(new StringReader(input));

        if (tokens != null) {
            var actual = new ArrayList<String>();
            while (tokenizer.hasNext()) {
                actual.add(tokenizer.next().getValue());
            }
            assertIterableEquals(tokens, actual);
        } else {
            assertThrows(ParseException.class, () -> {
                while (tokenizer.hasNext()) {
                    tokenizer.next();
                }
            });
        }
    }

    static Stream<Arguments> testVariousGrammars() {
        // Pattern
        var stream = Stream.<Arguments>of(
                arguments(Grammar.builder()
                        .add("A", pattern("0"))
                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(Grammar.builder()
                        .add("A", pattern("0"))
                        .build(), "0001", null),
                arguments(Grammar.builder()
                        .add("A", pattern("."))
                        .build(), "0123", List.of("0", "1", "2", "3")),
                arguments(Grammar.builder()
                        .add("A", pattern(Pattern.compile(".")))
                        .build(), "0123", List.of("0", "1", "2", "3")),
                arguments(Grammar.builder()
                        .add("A", pattern(Pattern.compile(".", Pattern.LITERAL)))
                        .build(), "....", List.of(".", ".", ".", ".")),
                arguments(Grammar.builder()
                        .add("A", pattern(Pattern.compile(".", Pattern.LITERAL)))
                        .build(), "0123", null),
                arguments(Grammar.builder()
                        .add("A", pattern(""))
                        .build(), "", List.of()),
                arguments(Grammar.builder()
                        .add("A", pattern(""))
                        .build(), "0123", null));
        // + Sequence
        stream = Stream.concat(stream, Stream.of(
                arguments(Grammar.builder()
                        .add("A", sequenceBuilder()
                                .add(pattern("0")))
                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(Grammar.builder()
                        .add("A", sequenceBuilder()
                                .add(pattern("0"))
                                .add(pattern("0")))
                        .build(), "0000", List.of("00", "00")),
                arguments(Grammar.builder()
                        .add("A", sequenceBuilder()
                                .add(pattern("0"))
                                .add(pattern("0")))
                        .build(), "000", null),
                arguments(Grammar.builder()
                        .add("A", sequenceBuilder()
                                .add(pattern("0"))
                                .add(pattern("1")))
                        .build(), "0101", List.of("01", "01"))));
        // + Choice
        stream = Stream.concat(stream, Stream.of(
                arguments(Grammar.builder()
                        .add("A", choiceBuilder()
                                .add(pattern("0")))
                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(Grammar.builder()
                        .add("A", choiceBuilder()
                                .add(pattern("0"))
                                .add(pattern("1")))
                        .build(), "0110", List.of("0", "1", "1", "0")),
                arguments(Grammar.builder()
                        .add("A", choiceBuilder()
                                .add(pattern("0"))
                                .addEmpty())
                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(Grammar.builder()
                        .add("A", choiceBuilder()
                                .add(pattern("0"))
                                .add(pattern("[^0]")))
                        .build(), "0123", List.of("0", "1", "2", "3")),
                arguments(Grammar.builder()
                        .add("A", sequenceBuilder()
                                .add(choiceBuilder()
                                        .add(pattern("0"))
                                        .addEmpty())
                                .add(pattern("1")))
                        .build(), "011101", List.of("01", "1", "1", "01")),
                arguments(Grammar.builder()
                        .add("A", pattern("0"))
                        .add("A", pattern("1"))
                        .build(), "0110", List.of("0", "1", "1", "0"))));
        // + Reference
        stream = Stream.concat(stream, Stream.of(
                arguments(Grammar.builder()
                        .add("A", reference("B"))
                        .add("B", pattern("0"))
                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(Grammar.builder()
                        .add("A", sequenceBuilder()
                                .add(pattern("0"))
                                .add(choiceBuilder()
                                        .addEmpty()
                                        .add(reference("A"))))
                        .build(), "0000", List.of("0000"))));
        // + Quantifier
        stream = Stream.concat(stream, Stream.of(
                arguments(Grammar.builder()
                        .add("A", pattern("0").opt())
                        .build(), "", List.of()),
                arguments(Grammar.builder()
                        .add("A", pattern("0").opt())
                        .build(), "0", List.of("0")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").opt())
                        .build(), "00", List.of("0", "0")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").zeroOrMore())
                        .build(), "", List.of()),
                arguments(Grammar.builder()
                        .add("A", pattern("0").zeroOrMore())
                        .build(), "0", List.of("0")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").zeroOrMore())
                        .build(), "00", List.of("00")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").oneOrMore())
                        .build(), "1", null),
                arguments(Grammar.builder()
                        .add("A", pattern("0").oneOrMore())
                        .build(), "0", List.of("0")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").oneOrMore())
                        .build(), "00", List.of("00")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").atLeast(2))
                        .build(), "0", null),
                arguments(Grammar.builder()
                        .add("A", pattern("0").atLeast(2))
                        .build(), "00", List.of("00")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").atLeast(2))
                        .build(), "000", List.of("000")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").exactly(2))
                        .build(), "0", null),
                arguments(Grammar.builder()
                        .add("A", pattern("0").exactly(2))
                        .build(), "00", List.of("00")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").exactly(2))
                        .build(), "000", null),
                arguments(Grammar.builder()
                        .add("A", pattern("0").exactly(2))
                        .build(), "0000", List.of("00", "00")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").range(2, 3))
                        .build(), "0", null),
                arguments(Grammar.builder()
                        .add("A", pattern("0").range(2, 3))
                        .build(), "00", List.of("00")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").range(2, 3))
                        .build(), "000", List.of("000")),
                arguments(Grammar.builder()
                        .add("A", pattern("0").range(2, 3))
                        .build(), "0000", null),
                arguments(Grammar.builder()
                        .add("A", pattern("0").range(2, 3))
                        .build(), "00000", List.of("000", "00"))));
        // + More
        stream = Stream.concat(stream, Stream.of(
                arguments(Grammar.builder()
                        .setSkipPattern("\\s")
                        .add("A", pattern("0"))
                        .build(), "0 00 0", List.of("0", "0", "0", "0")),
                arguments(Grammar.builder()
                        .add("A",
                                pattern("[\\x{%X}-\\x{%X}]".formatted(
                                        Character.MIN_SUPPLEMENTARY_CODE_POINT,
                                        Character.MAX_CODE_POINT)))
                        .build(), "🌟𝒜", List.of("🌟", "𝒜")),
                arguments(Grammar.builder()
                        .add("A", pattern("."))
                        .build(), String.valueOf("🌟".charAt(0)),
                        List.of(String.valueOf("🌟".charAt(0)))),
                arguments(Grammar.builder()
                        .add("A", pattern("."))
                        .build(), String.valueOf("🌟".charAt(0)) + "0",
                        List.of(String.valueOf("🌟".charAt(0)), "0"))));
        return stream;
    }

    @Test
    @DisplayName("Test decoration")
    void testDecoration() throws Exception {
        var grammar = Grammar.builder()
                .setSkipPattern("\\s")
                .add("A", pattern("0|1").exactly(2))
                .build();
        var tokenizer = TokenizerFactory.newFactory(grammar)
                .createTokenizer(new StringReader("00 01 10 11"));

        var grammar2 = Grammar.builder()
                .add("A", pattern("0|1"))
                .build();
        var tokenizer2 = TokenizerFactory.newFactory(grammar2)
                .createTokenizer(tokenizer);

        assertEquals("0", tokenizer2.next().getValue());
        assertEquals("0", tokenizer2.next().getValue());
        assertEquals("0", tokenizer2.next().getValue());
        assertEquals("1", tokenizer2.next().getValue());
        assertEquals("1", tokenizer2.next().getValue());
        assertEquals("0", tokenizer2.next().getValue());
        assertEquals("1", tokenizer2.next().getValue());
        assertEquals("1", tokenizer2.next().getValue());
        assertFalse(tokenizer2.hasNext());
    }
}
