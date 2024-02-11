package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.grammar.Rules.choiceBuilder;
import static com.jiganaut.bonsai.grammar.Rules.pattern;
import static com.jiganaut.bonsai.grammar.Rules.reference;
import static com.jiganaut.bonsai.grammar.Rules.sequenceBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.parser.Tree.Kind;

class ParserTest {

    static Stream<Arguments> allMethods() {
        return Stream.of(arguments(named("parse()", (Consumer<Parser>) p -> p.parse())));
    }

    @DisplayName("[Stream closed]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void streamClosed(Consumer<Parser> method) throws Exception {
        var factory = ParserFactory.newFactory(Stubs.DUMMY_GRAMMAR);
        var parser = factory.createParser(Stubs.closedReader());

        assertThrows(UncheckedIOException.class, () -> method.accept(parser));
    }

    @DisplayName("[Ambiguous rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void ambiguousRule(Consumer<Parser> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", choiceBuilder()
                        .add(pattern("1"))
                        .add(pattern(".")))
                .build();
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(parser));
    }

    @DisplayName("[Occurrence count out of range]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void occurrenceCountOutOfRange(Consumer<Parser> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1").range(3, 5))
                .build();
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader("11"));

        assertThrows(ParseException.class, () -> method.accept(parser));
    }

    @DisplayName("[Token not match pattern rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void tokenNotMatchPatternRule(Consumer<Parser> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("0"))
                .build();
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(parser));
    }

    @DisplayName("[Token not match choice rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void tokenNotMatchChoiceRule(Consumer<Parser> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", choiceBuilder()
                        .add(pattern("0")))
                .build();
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(parser));
    }

    @DisplayName("[Token not match sequence rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void tokenNotMatchSequenceRule(Consumer<Parser> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", sequenceBuilder()
                        .add(pattern("0")))
                .build();
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(parser));
    }

    @DisplayName("[Token not match empty rule]")
    @ParameterizedTest(name = "{0} {displayName}")
    @MethodSource("allMethods")
    void tokenNotMatchEmptyRule(Consumer<Parser> method) throws Exception {
        var grammar = Grammar.builder()
                .add("S", set -> Rule.EMPTY)
                .build();
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader("1"));

        assertThrows(ParseException.class, () -> method.accept(parser));
    }

    @Test
    @DisplayName("parse() [Token remained]")
    void parseInCaseToeknRemained() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader("11"));

        assertThrows(ParseException.class, () -> parser.parse());
    }

    @Test
    @DisplayName("parse()")
    void parse() throws Exception {
        var grammar = Grammar.builder()
                .add("S", pattern("1"))
                .build();
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader("1"));
        var tree = parser.parse();

        assertEquals(Kind.NON_TERMINAL, tree.getKind());
        assertTrue(tree.getKind().isNonTerminal());
        assertFalse(tree.getKind().isTerminal());
    }

    @DisplayName("Test various grammars.")
    @ParameterizedTest
    @MethodSource
    void testVariousGrammars(Grammar grammar, String input, String expected) {
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader(input));

        if (expected != null) {
            var actual = parser.parse().accept(new TreeVisitor<String, Void>() {
                @Override
                public String visitNonTerminal(NonTerminal tree, Void p) {
                    return tree.getSymbol() +
                            tree.getSubTrees().stream()
                                    .map(this::visit)
                                    .collect(Collectors.joining(",", "(", ")"));
                }

                @Override
                public String visitTerminal(Terminal tree, Void p) {
                    return tree.getValue();
                }
            });
            assertEquals(expected, actual);
        } else {
            assertThrows(ParseException.class, () -> parser.parse());
        }
    }

    static Stream<Arguments> testVariousGrammars() {
        var stream = Stream.<Arguments>of(
                arguments(Grammar.builder()
                        .add("A", pattern("0"))
                        .build(), "0", "A(0)"),
                arguments(Grammar.builder()
                        .add("A", pattern("0"))
                        .build(), "00", null),
                arguments(Grammar.builder()
                        .add("A", sequenceBuilder()
                                .add(pattern("0"))
                                .add(pattern("1")))
                        .build(), "01", "A(0,1)"),
                arguments(Grammar.builder()
                        .add("A", sequenceBuilder()
                                .add(choiceBuilder()
                                        .add(pattern("0"))
                                        .addEmpty())
                                .add(pattern("1")))
                        .build(), "01", "A(0,1)"),
                arguments(Grammar.builder()
                        .add("A", sequenceBuilder()
                                .add(choiceBuilder()
                                        .add(pattern("0"))
                                        .addEmpty())
                                .add(pattern("1")))
                        .build(), "1", "A(1)"),
                arguments(Grammar.builder()
                        .add("A", pattern("0"))
                        .add("A", sequenceBuilder()
                                .add(pattern("1"))
                                .add(reference("A")))
                        .build(), "10", "A(1,A(0))")
                );
        return stream;
    }
}
