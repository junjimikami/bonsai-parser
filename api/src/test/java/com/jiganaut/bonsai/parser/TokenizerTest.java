package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;

class TokenizerTest {

    static Stream<Arguments> allMethods() {
        return Stream.of(
                arguments(named("hasNext()", (Consumer<Tokenizer>) t -> t.hasNext())),
                arguments(named("hasNext(String)", (Consumer<Tokenizer>) t -> t.hasNext(""))),
                arguments(named("hasNext(Pattern)", (Consumer<Tokenizer>) t -> t.hasNext(Pattern.compile("")))),
                arguments(named("next()", (Consumer<Tokenizer>) t -> t.next())),
                arguments(named("next(String)", (Consumer<Tokenizer>) t -> t.next(""))),
                arguments(named("next(Pattern)", (Consumer<Tokenizer>) t -> t.next(Pattern.compile("")))));
    }

    @DisplayName("[Stream closed]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void streamClosed(Consumer<Tokenizer> method) throws Exception {
        var factory = TokenizerFactory.of(Stubs.DUMMY_PRODUCTION_SET);
        var tokenizer = factory.createTokenizer(Stubs.closedReader());

        assertThrows(UncheckedIOException.class, () -> method.accept(tokenizer));
    }

    @DisplayName("[Ambiguous rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void ambiguousRule(Consumer<Tokenizer> method) throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", ChoiceRule.builder()
                        .add(() -> PatternRule.of("1"))
                        .add(() -> PatternRule.of(".")))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(tokenizer));
    }

    @DisplayName("[Occurrence count out of range]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void occurrenceCountOutOfRange(Consumer<Tokenizer> method) throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1").range(3, 5))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("11"));

        assertThrows(ParseException.class, () -> method.accept(tokenizer));
    }

//    @DisplayName("[Token not match pattern rule]")
//    @ParameterizedTest(name = "{0} {displayName}")
//    @MethodSource("allMethods")
//    void tokenNotMatchPatternRule(Consumer<Tokenizer> method) throws Exception {
//        var grammar = ProductionSet.builder()
//                .add("S", () -> PatternRule.of("0"))
//                .build();
//        var factory = TokenizerFactory.of(grammar);
//        var tokenizer = factory.createTokenizer(new StringReader("1"));
//
//        assertThrows(ParseException.class, () -> method.accept(tokenizer));
//    }

//    @DisplayName("[Token not match choice rule]")
//    @ParameterizedTest(name = "{0} {displayName}")
//    @MethodSource("allMethods")
//    void tokenNotMatchChoiceRule(Consumer<Tokenizer> method) throws Exception {
//        var grammar = ProductionSet.builder()
//                .add("S", ChoiceRule.builder()
//                        .add(() -> PatternRule.of("0")))
//                .build();
//        var factory = TokenizerFactory.of(grammar);
//        var tokenizer = factory.createTokenizer(new StringReader("1"));
//
//        assertThrows(ParseException.class, () -> method.accept(tokenizer));
//    }

//    @DisplayName("[Token not match sequence rule]")
//    @ParameterizedTest(name = "{0} {displayName}")
//    @MethodSource("allMethods")
//    void tokenNotMatchSequenceRule(Consumer<Tokenizer> method) throws Exception {
//        var grammar = ProductionSet.builder()
//                .add("S", SequenceRule.builder()
//                        .add(() -> PatternRule.of("0")))
//                .build();
//        var factory = TokenizerFactory.of(grammar);
//        var tokenizer = factory.createTokenizer(new StringReader("1"));
//
//        assertThrows(ParseException.class, () -> method.accept(tokenizer));
//    }

//    @DisplayName("[Token not match empty rule]")
//    @ParameterizedTest(name = "{0} {displayName}")
//    @MethodSource("allMethods")
//    void tokenNotMatchEmptyRule(Consumer<Tokenizer> method) throws Exception {
//        var grammar = ProductionSet.builder()
//                .add("S", () -> Rule.EMPTY)
//                .build();
//        var factory = TokenizerFactory.of(grammar);
//        var tokenizer = factory.createTokenizer(new StringReader("1"));
//
//        assertThrows(ParseException.class, () -> method.accept(tokenizer));
//    }

    @Test
    @DisplayName("hasNext(st:String) [Null parameter]")
    void hasNextStInCaseNullParameter() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.hasNext((String) null));
    }

    @Test
    @DisplayName("hasNext(pa:Pattern) [Null parameter]")
    void hasNextPaInCaseNullParameter() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.hasNext((Pattern) null));
    }

    @Test
    @DisplayName("next(st:String) [Null parameter]")
    void nextStInCaseNullParameter() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.next((String) null));
    }

    @Test
    @DisplayName("next(pa:Pattern) [Null parameter]")
    void nextPaInCaseNullParameter() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NullPointerException.class, () -> tokenizer.next((Pattern) null));
    }

    @Test
    @DisplayName("next(st:String) [No tokens matching pattern]")
    void nextStInCaseNoTokensMatchingPattern() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertThrows(NoSuchElementException.class, () -> tokenizer.next("2"));
    }

    @Test
    @DisplayName("next(pa:Pattern) [No tokens matching pattern]")
    void nextPaInCaseNoTokensMatchingPattern() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("2");

        assertThrows(NoSuchElementException.class, () -> tokenizer.next(pattern));
    }

    @Test
    @DisplayName("hasNext() [Token remaining]")
    void hasNextInCaseTokenRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertTrue(tokenizer.hasNext());
    }

    @Test
    @DisplayName("hasNext(st:String) [Token remaining]")
    void hasNextStInCaseTokenRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertTrue(tokenizer.hasNext("1"));
    }

    @Test
    @DisplayName("hasNext(pa:Pattern) [Token remaining]")
    void hasNextPaInCaseTokenRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("1");

        assertTrue(tokenizer.hasNext(pattern));
    }

    @Test
    @DisplayName("hasNext() [No tokens remaining]")
    void hasNextInCaseNoTokensRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());

        assertFalse(tokenizer.hasNext());
    }

    @Test
    @DisplayName("hasNext(st:String) [No tokens remaining]")
    void hasNextStInCaseNoTokensRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());

        assertFalse(tokenizer.hasNext(""));
    }

    @Test
    @DisplayName("hasNext(pa:Pattern) [No tokens remaining]")
    void hasNextPaInCaseNoTokensRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());
        var pattern = Pattern.compile("");

        assertFalse(tokenizer.hasNext(pattern));
    }

    @Test
    @DisplayName("next() [Token remaining]")
    void nextInCaseTokenRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertEquals("S", tokenizer.next());
        assertEquals("1", tokenizer.getValue());
    }

    @Test
    @DisplayName("next(st:String) [Token remaining]")
    void nextStInCaseTokenRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertEquals("S", tokenizer.next("1"));
        assertEquals("1", tokenizer.getValue());
    }

    @Test
    @DisplayName("next(pa:Pattern) [Token remaining]")
    void nextPaInCaseTokenRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("1");

        assertEquals("S", tokenizer.next(pattern));
        assertEquals("1", tokenizer.getValue());
    }

    @Test
    @DisplayName("next() [No tokens remaining]")
    void nextInCaseNoTokensRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());

        assertThrows(NoSuchElementException.class, () -> tokenizer.next());
    }

    @Test
    @DisplayName("next(st:String) [No tokens remaining]")
    void nextStInCaseNoTokensRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());

        assertThrows(NoSuchElementException.class, () -> tokenizer.next(""));
    }

    @Test
    @DisplayName("next(pa:Pattern) [No tokens remaining]")
    void nextPaInCaseNoTokensRemaining() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> Rule.EMPTY)
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(Reader.nullReader());
        var pattern = Pattern.compile("");

        assertThrows(NoSuchElementException.class, () -> tokenizer.next(pattern));
    }

    @Test
    @DisplayName("hasNext()")
    void hasNext() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertTrue(tokenizer.hasNext());
    }

    @Test
    @DisplayName("hasNext(st:String)")
    void hasNextSt() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));

        assertTrue(tokenizer.hasNext("1"));
    }

    @Test
    @DisplayName("hasNext(pa:Pattern)")
    void hasNextPa() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("1");

        assertTrue(tokenizer.hasNext(pattern));
    }

    @Test
    @DisplayName("next()")
    void next() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        tokenizer.next();
        var token = tokenizer.getToken();

        assertEquals(Tree.Kind.TERMINAL, token.getKind());
        assertTrue(token.getKind().isTerminal());
        assertFalse(token.getKind().isNonTerminal());
        assertEquals("1", token.getValue());
    }

    @Test
    @DisplayName("next(st:String)")
    void nextSt() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        tokenizer.next("1");
        var token = tokenizer.getToken();

        assertEquals(Tree.Kind.TERMINAL, token.getKind());
        assertEquals("1", token.getValue());

    }

    @Test
    @DisplayName("next(pa:Pattern)")
    void nextPa() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of("1"))
                .build();
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader("1"));
        var pattern = Pattern.compile("1");
        tokenizer.next(pattern);
        var token = tokenizer.getToken();

        assertEquals(Tree.Kind.TERMINAL, token.getKind());
        assertEquals("1", token.getValue());
    }

    @DisplayName("Test various grammars")
    @ParameterizedTest
    @MethodSource
    void testVariousGrammars(ProductionSet grammar, String input, List<String> tokens) {
        var factory = TokenizerFactory.of(grammar);
        var tokenizer = factory.createTokenizer(new StringReader(input));

        if (tokens != null) {
            var actual = new ArrayList<String>();
            while (tokenizer.hasNext()) {
                tokenizer.next();
                actual.add(tokenizer.getValue());
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
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0"))
                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0"))
                        .build(), "0001", List.of("0", "0", "0", "1")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("."))
                        .build(), "0123", List.of("0", "1", "2", "3")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of(Pattern.compile(".")))
                        .build(), "0123", List.of("0", "1", "2", "3")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of(Pattern.compile(".", Pattern.LITERAL)))
                        .build(), "....", List.of(".", ".", ".", ".")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of(Pattern.compile(".", Pattern.LITERAL)))
                        .build(), "0123", List.of("0", "1", "2", "3")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of(""))
                        .build(), "", List.of()),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of(""))
                        .build(), "0123", List.of("0", "1", "2", "3")));
        // + Sequence
        stream = Stream.concat(stream, Stream.of(
                arguments(ProductionSet.builder()
                        .add("A", SequenceRule.builder()
                                .add(() -> PatternRule.of("0")))
                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(ProductionSet.builder()
                        .add("A", SequenceRule.builder()
                                .add(() -> PatternRule.of("0"))
                                .add(() -> PatternRule.of("0")))
                        .build(), "0000", List.of("00", "00")),
                arguments(ProductionSet.builder()
                        .add("A", SequenceRule.builder()
                                .add(() -> PatternRule.of("0"))
                                .add(() -> PatternRule.of("0")))
                        .build(), "000", null),
                arguments(ProductionSet.builder()
                        .add("A", SequenceRule.builder()
                                .add(() -> PatternRule.of("0"))
                                .add(() -> PatternRule.of("1")))
                        .build(), "0101", List.of("01", "01"))));
        // + Choice
        stream = Stream.concat(stream, Stream.of(
                arguments(ProductionSet.builder()
                        .add("A", ChoiceRule.builder()
                                .add(() -> PatternRule.of("0")))
                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(ProductionSet.builder()
                        .add("A", ChoiceRule.builder()
                                .add(() -> PatternRule.of("0"))
                                .add(() -> PatternRule.of("1")))
                        .build(), "0110", List.of("0", "1", "1", "0")),
                arguments(ProductionSet.builder()
                        .add("A", ChoiceRule.builder()
                                .add(() -> PatternRule.of("0"))
                                .addEmpty())
                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(ProductionSet.builder()
                        .add("A", ChoiceRule.builder()
                                .add(() -> PatternRule.of("0"))
                                .add(() -> PatternRule.of("[^0]")))
                        .build(), "0123", List.of("0", "1", "2", "3")),
                arguments(ProductionSet.builder()
                        .add("A", SequenceRule.builder()
                                .add(ChoiceRule.builder()
                                        .add(() -> PatternRule.of("0"))
                                        .addEmpty())
                                .add(() -> PatternRule.of("1")))
                        .build(), "011101", List.of("01", "1", "1", "01")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0"))
                        .add("A", () -> PatternRule.of("1"))
                        .build(), "0110", List.of("0", "1", "1", "0"))));
        // + Reference
        stream = Stream.concat(stream, Stream.of(
//                arguments(ProductionSet.builder()
//                        .add("A", () -> ReferenceRule.of("B"))
//                        .add("B", () -> PatternRule.of("0"))
//                        .build(), "0000", List.of("0", "0", "0", "0")),
                arguments(ProductionSet.builder()
                        .add("A", SequenceRule.builder()
                                .add(() -> PatternRule.of("0"))
                                .add(ChoiceRule.builder()
                                        .addEmpty()
                                        .add(() -> ReferenceRule.of("A"))))
                        .build(), "0000", List.of("0000"))));
        // + Quantifier
        stream = Stream.concat(stream, Stream.of(
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").opt())
                        .build(), "", List.of()),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").opt())
                        .build(), "0", List.of("0")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").opt())
                        .build(), "00", List.of("0", "0")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").zeroOrMore())
                        .build(), "", List.of()),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").zeroOrMore())
                        .build(), "0", List.of("0")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").zeroOrMore())
                        .build(), "00", List.of("00")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").oneOrMore())
                        .build(), "1", List.of("1")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").oneOrMore())
                        .build(), "0", List.of("0")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").oneOrMore())
                        .build(), "00", List.of("00")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").atLeast(2))
                        .build(), "0", null),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").atLeast(2))
                        .build(), "00", List.of("00")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").atLeast(2))
                        .build(), "000", List.of("000")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").exactly(2))
                        .build(), "0", null),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").exactly(2))
                        .build(), "00", List.of("00")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").exactly(2))
                        .build(), "000", null),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").exactly(2))
                        .build(), "0000", List.of("00", "00")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").range(2, 3))
                        .build(), "0", null),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").range(2, 3))
                        .build(), "00", List.of("00")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").range(2, 3))
                        .build(), "000", List.of("000")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").range(2, 3))
                        .build(), "0000", null),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").range(2, 3))
                        .build(), "00000", List.of("000", "00"))));
        // + Skip
        stream = Stream.concat(stream, Stream.of(
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("0").skip())
                        .build(), "0", List.of())));
        // + More
        stream = Stream.concat(stream, Stream.of(
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("[\\x{%X}-\\x{%X}]".formatted(
                                Character.MIN_SUPPLEMENTARY_CODE_POINT,
                                Character.MAX_CODE_POINT)))
                        .build(), "🌟𝒜", List.of("🌟", "𝒜")),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("."))
                        .build(), String.valueOf("🌟".charAt(0)),
                        List.of(String.valueOf("🌟".charAt(0)))),
                arguments(ProductionSet.builder()
                        .add("A", () -> PatternRule.of("."))
                        .build(), String.valueOf("🌟".charAt(0)) + "0",
                        List.of(String.valueOf("🌟".charAt(0)), "0")),
                arguments(ProductionSet.builder()
                        .add("A", SequenceRule.builder()
                                .add(SequenceRule.builder()
                                        .add(() -> PatternRule.of("0"))
                                        .add(() -> PatternRule.of("0")))
                                .add(() -> PatternRule.of("0"))
                                .add(() -> PatternRule.of("1")))
                        .build(), "00010001", List.of("0001", "0001")),
                arguments(ProductionSet.builder()
                        .add("A", SequenceRule.builder()
                                .add(ChoiceRule.builder()
                                        .add(() -> PatternRule.of("0"))
                                        .add(() -> PatternRule.of("1")))
                                .add(() -> PatternRule.of("0"))
                                .add(() -> PatternRule.of("1")))
                        .build(), "001101", List.of("001", "101")),
                arguments(ProductionSet.builder()
                        .add("A", ChoiceRule.builder()
                                .add(SequenceRule.builder()
                                        .add(() -> PatternRule.of("0"))
                                        .add(() -> PatternRule.of("1")))
                                .add(() -> PatternRule.of("1")))
                        .build(), "01101", List.of("01", "1", "01")),
                arguments(ProductionSet.builder()
                        .add("A", ChoiceRule.builder()
                                .add(ChoiceRule.builder()
                                        .add(() -> PatternRule.of("0"))
                                        .add(() -> PatternRule.of("1")))
                                .add(() -> PatternRule.of("2")))
                        .build(), "01201", List.of("0", "1", "2", "0", "1"))
                ));
        return stream;
    }

    @Test
    @DisplayName("Test decoration")
    void testDecoration() throws Exception {
        var grammar = ProductionSet.builder()
                .add("A", () -> PatternRule.of("0|1").exactly(2))
                .add("A", () -> PatternRule.of("\\s").skip())
                .build();
        var tokenizer = TokenizerFactory.of(grammar)
                .createTokenizer(new StringReader("00 01 10 11"));

        var grammar2 = ProductionSet.builder()
                .add("A", () -> PatternRule.of("(0|1){2}").exactly(2))
                .build();
        var tokenizer2 = TokenizerFactory.of(grammar2)
                .createTokenizer(tokenizer);

        tokenizer2.next();
        assertEquals("0001", tokenizer2.getValue());
        tokenizer2.next();
        assertEquals("1011", tokenizer2.getValue());
        assertFalse(tokenizer2.hasNext());
    }

}
