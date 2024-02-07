package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.grammar.Rules.choiceBuilder;
import static com.jiganaut.bonsai.grammar.Rules.pattern;
import static com.jiganaut.bonsai.grammar.Rules.sequenceBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
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
                        .add("A")
                        .add("B"))
                .add("A", pattern("1"))
                .add("B", pattern("."))
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
    }

}
