package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.function.Executable;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Quantifiable;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rules;
import com.jiganaut.bonsai.grammar.SequenceRule;

/**
 *
 * @author Junji Mikami
 */
class ParserTest {

    @Nested
    class ParseExceptionTestCase1 implements ParseExceptionTestCase {

        @Override
        public Executable createTarget(Grammar<String> grammar, Reader reader) {
            var factory = ParserFactory.of(grammar);
            var parser = factory.createParser(TextSource.of(reader));
            return () -> parser.parse();
        }

        @Test
        @DisplayName("throws ParseException when tokens remain")
        void throwsParseExceptionWhenTokensRemain(TestReporter testReporter) throws Exception {
            var grammar = Grammar.<String>builder("S")
                    .add("S", () -> Rules.pattern("1"))
                    .build();
            var factory = ParserFactory.of(grammar);
            var parser = factory.createParser(TextSource.of(new StringReader("12")));

            var ex = assertThrows(ParseException.class, () -> parser.parse());
            testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
        }

    }

    @Nested
    class TestCase1 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder("S")
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("A")))
                    .add("A", Rules.pattern("0"))
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("00")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("S")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .add(NonTerminalNode.<String>builder("A")
                            .add(Token.ofUnnamed("0", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                            .build())
                    .build();
        }

    }

    @Nested
    class TestCase2 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder("S")
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("A")))
                    .add("S", Rules.pattern("1"))
                    .add("A", Rules.pattern("0"))
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("00")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("S")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .add(NonTerminalNode.<String>builder("A")
                            .add(Token.ofUnnamed("0", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                            .build())
                    .build();
        }

    }

    @Nested
    class TestCase3 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder("S")
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("A")))
                    .add("S", Rules.pattern("1"))
                    .add("A", Rules.pattern("0"))
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("1")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("S")
                    .add(Token.ofUnnamed("1", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .build();
        }

    }

    @Nested
    class TestCase4 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder("S")
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("A")))
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("B")))
                    .add("A", Rules.pattern("1"))
                    .add("B", Rules.pattern("2"))
                    .asShortCircuit()
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("01")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("S")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .add(NonTerminalNode.<String>builder("A")
                            .add(Token.ofUnnamed("1", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                            .build())
                    .build();
        }

    }

    @Nested
    class TestCase5 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder("S")
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("A")))
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("B")))
                    .add("A", Rules.pattern("1"))
                    .add("B", Rules.pattern("2"))
                    .asShortCircuit()
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("02")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("S")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .add(NonTerminalNode.<String>builder("B")
                            .add(Token.ofUnnamed("2", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                            .build())
                    .build();
        }

    }

    @Nested
    class TestCase6 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder("S")
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("A")))
                    .add("S", ChoiceRule.<String>builder()
                            .add(SequenceRule.<String>builder()
                                    .add(Rules.pattern("0"))
                                    .add(ReferenceRule.of("C")))
                            .add(SequenceRule.<String>builder()
                                    .add(Rules.pattern("0"))
                                    .add(ReferenceRule.of("B")))
                            .asShortCircuit())
                    .add("A", Rules.pattern("1"))
                    .add("B", Rules.pattern("2"))
                    .add("C", Rules.pattern("3"))
                    .asShortCircuit()
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("02")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("S")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .add(NonTerminalNode.<String>builder("B")
                            .add(Token.ofUnnamed("2", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                            .build())
                    .build();
        }

    }

    @Nested
    class TestCase7 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("B", Rules.pattern("1"))
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("0")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("A")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .build();
        }

    }

    @Nested
    class TestCase8 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("B", Rules.pattern("1"))
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("1")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("B")
                    .add(Token.ofUnnamed("1", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .build();
        }

    }

    @Nested
    class TestCase9 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("A", Rules.pattern("1"))
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("0")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("A")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .build();
        }

    }

    @Nested
    class TestCase10 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("A", Rules.pattern("1"))
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("1")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("A")
                    .add(Token.ofUnnamed("1", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .build();
        }

    }

    @Nested
    class TestCase11 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("B", Rules.pattern("0"))
                    .asShortCircuit()
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("0")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("A")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .build();
        }

    }

    @Nested
    class TestCase12 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(Rules.pattern("1"))
                            .add(Rules.pattern("8")))
                    .add("B", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(Rules.pattern("1"))
                            .add(Rules.pattern("9")))
                    .asShortCircuit()
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("018")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("A")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .add(Token.ofUnnamed("1", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                    .add(Token.ofUnnamed("8", Position.of(2, 1, 3).withRangeEnd(3, 1, 4)))
                    .build();
        }

    }

    @Nested
    class TestCase13 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(Rules.pattern("1"))
                            .add(Rules.pattern("8")))
                    .add("B", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(Rules.pattern("1"))
                            .add(Rules.pattern("9")))
                    .asShortCircuit()
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("019")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("B")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .add(Token.ofUnnamed("1", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                    .add(Token.ofUnnamed("9", Position.of(2, 1, 3).withRangeEnd(3, 1, 4)))
                    .build();
        }

    }

    @Nested
    class TestCase14 implements ParserTestCase<String> {

        @Override
        public Parser<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(Rules.pattern("1"))
                            .add(Rules.pattern("8")))
                    .add("B", ChoiceRule.<String>builder()
                            .add(SequenceRule.<String>builder()
                                    .add(Rules.pattern("0"))
                                    .add(Rules.pattern("1"))
                                    .add(Rules.pattern("7")))
                            .add(SequenceRule.<String>builder()
                                    .add(Rules.pattern("0"))
                                    .add(Rules.pattern("1"))
                                    .add(Rules.pattern("9")))
                            .asShortCircuit())
                    .asShortCircuit()
                    .build();
            var factory = ParserFactory.of(grammar);
            return factory.createParser(TextSource.of(new StringReader("019")));
        }

        @Override
        public Tree<String> expectedTree() {
            return NonTerminalNode.<String>builder("B")
                    .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                    .add(Token.ofUnnamed("1", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                    .add(Token.ofUnnamed("9", Position.of(2, 1, 3).withRangeEnd(3, 1, 4)))
                    .build();
        }

    }

    @Nested
    class EmptyRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.empty())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(Reader.nullReader()));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .build();
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.empty())
                                .add(Rules.pattern("0")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("0")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.empty())
                                .add(Rules.pattern("1")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(Reader.nullReader()));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .build();
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(ChoiceRule.<String>builder()
                                        .add(Rules.pattern("1"))
                                        .add(Rules.empty()))
                                .add(Rules.pattern("0")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("0")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

    }

    @Nested
    class MatchingRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("0")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern("."))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("0")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern(Pattern.compile(".", Pattern.LITERAL)))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(".")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed(".", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern("𝒜"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("𝒜")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("𝒜", Position.of(0, 1, 1).withRangeEnd(2, 1, 3)))
                        .build();
            }

        }

    }

    @Nested
    class SequenceRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.pattern("0")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("0")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .add(Rules.pattern("1")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("01")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .add(Token.ofUnnamed("1", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                        .build();
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .add(SequenceRule.<String>builder()
                                        .add(Rules.pattern("1"))
                                        .add(Rules.pattern("2"))))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("012")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .add(Token.ofUnnamed("1", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                        .add(Token.ofUnnamed("2", Position.of(2, 1, 3).withRangeEnd(3, 1, 4)))
                        .build();
            }

        }

    }

    @Nested
    class ChoiceRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("0")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("0")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .add(Rules.pattern("1")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("1")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("1", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .addEmpty())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(Reader.nullReader()));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .build();
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .add(ChoiceRule.<String>builder()
                                        .add(Rules.pattern("1"))
                                        .add(Rules.pattern("2"))))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("2")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("2", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

        @Nested
        class TestCase5 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("1"))
                                .add(Rules.pattern("."))
                                .asShortCircuit())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("1")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("1", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .build();
            }

        }

        @Nested
        class TestCase6 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(SequenceRule.<String>builder()
                                        .add(Rules.pattern("0"))
                                        .add(Rules.pattern("1"))
                                        .add(Rules.pattern("8")))
                                .add(SequenceRule.<String>builder()
                                        .add(Rules.pattern("0"))
                                        .add(Rules.pattern("1"))
                                        .add(Rules.pattern("9")))
                                .asShortCircuit())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("019")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .add(Token.ofUnnamed("1", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                        .add(Token.ofUnnamed("9", Position.of(2, 1, 3).withRangeEnd(3, 1, 4)))
                        .build();
            }

        }

        @Nested
        class TestCase7 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(SequenceRule.<String>builder()
                                        .add(Rules.pattern("0"))
                                        .add(Rules.pattern("1"))
                                        .add(Rules.pattern("8")))
                                .add(ChoiceRule.<String>builder()
                                        .add(SequenceRule.<String>builder()
                                                .add(Rules.pattern("0"))
                                                .add(Rules.pattern("1"))
                                                .add(Rules.pattern("7")))
                                        .add(SequenceRule.<String>builder()
                                                .add(Rules.pattern("0"))
                                                .add(Rules.pattern("1"))
                                                .add(Rules.pattern("9")))
                                        .asShortCircuit())
                                .asShortCircuit())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("019")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                        .add(Token.ofUnnamed("1", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                        .add(Token.ofUnnamed("9", Position.of(2, 1, 3).withRangeEnd(3, 1, 4)))
                        .build();
            }

        }

    }

    @Nested
    class ReferenceRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ReferenceRule.of("A"))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("0")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(NonTerminalNode.<String>builder("A")
                                .add(Token.ofUnnamed("0", Position.of(0, 1, 1).withRangeEnd(1, 1, 2)))
                                .build())
                        .build();
            }

        }

    }

    abstract class ParserTestCaseForQuantifierRule {

        abstract Quantifiable<String> createRule();

        abstract String createInput();

        abstract Stream<Tree<String>> expectedSubTree(int times);

        @Nested
        class TestCase1 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().opt())
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(createInput().repeat(0))));
            }

            @Override
            public Tree<String> expectedTree() {
                var builder = NonTerminalNode.<String>builder("S");
                expectedSubTree(0).forEach(builder::add);
                return builder.build();
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().zeroOrMore())
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(createInput().repeat(0))));
            }

            @Override
            public Tree<String> expectedTree() {
                var builder = NonTerminalNode.<String>builder("S");
                expectedSubTree(0).forEach(builder::add);
                return builder.build();
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().oneOrMore())
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(createInput().repeat(1))));
            }

            @Override
            public Tree<String> expectedTree() {
                var builder = NonTerminalNode.<String>builder("S");
                expectedSubTree(1).forEach(builder::add);
                return builder.build();
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().oneOrMore())
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(createInput().repeat(2))));
            }

            @Override
            public Tree<String> expectedTree() {
                return expectedSubTree(2)
                        .reduce(NonTerminalNode.<String>builder("S"),
                                NonTerminalNode.Builder::add,
                                NonTerminalNode.Builder::addAll)
                        .build();
            }

        }

        @Nested
        class TestCase5 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().exactly(2))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(createInput().repeat(2))));
            }

            @Override
            public Tree<String> expectedTree() {
                var builder = NonTerminalNode.<String>builder("S");
                expectedSubTree(2).forEach(builder::add);
                return builder.build();
            }

        }

        @Nested
        class TestCase6 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().atLeast(1))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(createInput().repeat(1))));
            }

            @Override
            public Tree<String> expectedTree() {
                var builder = NonTerminalNode.<String>builder("S");
                expectedSubTree(1).forEach(builder::add);
                return builder.build();
            }

        }

        @Nested
        class TestCase7 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().atLeast(1))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(createInput().repeat(2))));
            }

            @Override
            public Tree<String> expectedTree() {
                var builder = NonTerminalNode.<String>builder("S");
                expectedSubTree(2).forEach(builder::add);
                return builder.build();
            }

        }

        @Nested
        class TestCase8 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().range(1, 2))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(createInput().repeat(1))));
            }

            @Override
            public Tree<String> expectedTree() {
                var builder = NonTerminalNode.<String>builder("S");
                expectedSubTree(1).forEach(builder::add);
                return builder.build();
            }

        }

        @Nested
        class TestCase9 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().range(1, 2))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader(createInput().repeat(2))));
            }

            @Override
            public Tree<String> expectedTree() {
                var builder = NonTerminalNode.<String>builder("S");
                expectedSubTree(2).forEach(builder::add);
                return builder.build();
            }

        }

    }

    @Nested
    class QuantifierRuleTestCase {

        @Nested
        class QuantifierRuleTestCase1 extends ParserTestCaseForQuantifierRule {

            @Override
            Quantifiable<String> createRule() {
                return Rules.pattern("0");
            }

            @Override
            String createInput() {
                return "0";
            }

            @Override
            Stream<Tree<String>> expectedSubTree(int times) {
                String[] values = {"0"};
                return IntStream.range(0, values.length * times)
                        .mapToObj(i -> Token.ofUnnamed(values[i % values.length], Position.of(i, 1, i + 1).withRangeEnd(i + 1, 1, i + 2)));
            }

        }

        @Nested
        class QuantifierRuleTestCase2 extends ParserTestCaseForQuantifierRule {

            @Override
            Quantifiable<String> createRule() {
                return SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(Rules.pattern("1"))
                        .build();
            }

            @Override
            String createInput() {
                return "01";
            }

            @Override
            Stream<Tree<String>> expectedSubTree(int times) {
                String[] values = {"0", "1"};
                return IntStream.range(0, values.length * times)
                        .mapToObj(i -> Token.ofUnnamed(values[i % values.length], Position.of(i, 1, i + 1).withRangeEnd(i + 1, 1, i + 2)));
            }

        }

        @Nested
        class QuantifierRuleTestCase3 extends ParserTestCaseForQuantifierRule {

            @Override
            Quantifiable<String> createRule() {
                return ChoiceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(Rules.pattern("1"))
                        .build();
            }

            @Override
            String createInput() {
                return "1";
            }

            @Override
            Stream<Tree<String>> expectedSubTree(int times) {
                String[] values = {"1"};
                return IntStream.range(0, values.length * times)
                        .mapToObj(i -> Token.ofUnnamed(values[i % values.length], Position.of(i, 1, i + 1).withRangeEnd(i + 1, 1, i + 2)));
            }

        }

        @Nested
        class QuantifierRuleTestCase4 extends ParserTestCaseForQuantifierRule {

            @Override
            Quantifiable<String> createRule() {
                return ReferenceRule.of("A");
            }

            @Override
            String createInput() {
                return "0";
            }

            @Override
            Stream<Tree<String>> expectedSubTree(int times) {
                String[] values = {"0"};
                return IntStream.range(0, values.length * times)
                        .mapToObj(i -> NonTerminalNode.<String>builder("A")
                                .add(Token.ofUnnamed(values[i % values.length], Position.of(i, 1, i + 1).withRangeEnd(i + 1, 1, i + 2)))
                                .build());
            }

        }

    }

    @Nested
    class SkipRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern("0").skip())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("0")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .build();
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase<String> {

            @Override
            public Parser<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.pattern("0").skip())
                                .add(Rules.pattern("1"))
                                .add(Rules.pattern("2").skip()))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(TextSource.of(new StringReader("012")));
            }

            @Override
            public Tree<String> expectedTree() {
                return NonTerminalNode.<String>builder("S")
                        .add(Token.ofUnnamed("1", Position.of(1, 1, 2).withRangeEnd(2, 1, 3)))
                        .build();
            }

        }

    }

}
