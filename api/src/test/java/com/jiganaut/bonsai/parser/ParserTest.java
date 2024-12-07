package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.function.Executable;

import com.jiganaut.bonsai.grammar.ChoiceGrammar;
import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.Quantifiable;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SingleOriginGrammar;

class ParserTest {

    @Nested
    class ParseExceptionTestCase1 implements ParseExceptionTestCase {

        @Override
        public Executable createTarget(Grammar grammar, Reader reader) {
            var factory = ParserFactory.of(grammar);
            var parser = factory.createParser(reader);
            return () -> parser.parse();
        }

        @Test
        @DisplayName("[Tokens remained]")
        void tokensRemained(TestReporter testReporter) throws Exception {
            var grammar = SingleOriginGrammar.builder()
                    .add("S", () -> PatternRule.of("1"))
                    .build();
            var factory = ParserFactory.of(grammar);
            var parser = factory.createParser(new StringReader("11"));

            var ex = assertThrows(ParseException.class, () -> parser.parse());
            testReporter.publishEntry(ex.getMessage());
        }

    }

    @Nested
    class SingleOriginGrammarTestCase {

        @Nested
        class TestCase1 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("A")))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("00"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("0"),
                        NonTerminalNode.of("A", TerminalNode.ofUnnamed("0")));
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("A")))
                        .add("S", PatternRule.of("1"))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("00"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("0"),
                        NonTerminalNode.of("A", TerminalNode.ofUnnamed("0")));
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("A")))
                        .add("S", PatternRule.of("1"))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("1"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("1"));
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("A")))
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("B")))
                        .add("A", PatternRule.of("1"))
                        .add("B", PatternRule.of("2"))
                        .shortCircuit();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("01"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("0"),
                        NonTerminalNode.of("A", TerminalNode.ofUnnamed("1")));
            }

        }

        @Nested
        class TestCase5 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("A")))
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("B")))
                        .add("A", PatternRule.of("1"))
                        .add("B", PatternRule.of("2"))
                        .shortCircuit();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("02"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("0"),
                        NonTerminalNode.of("B", TerminalNode.ofUnnamed("2")));
            }

        }

        @Nested
        class TestCase6 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("A")))
                        .add("S", ChoiceRule.builder()
                                .add(SequenceRule.builder()
                                        .add(PatternRule.of("0"))
                                        .add(ReferenceRule.of("C")))
                                .add(SequenceRule.builder()
                                        .add(PatternRule.of("0"))
                                        .add(ReferenceRule.of("B")))
                                .shortCircuit())
                        .add("A", PatternRule.of("1"))
                        .add("B", PatternRule.of("2"))
                        .add("C", PatternRule.of("3"))
                        .shortCircuit();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("02"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("0"),
                        NonTerminalNode.of("B", TerminalNode.ofUnnamed("2")));
            }

        }

    }

    @Nested
    class ChoiceGrammarTestCase {

        @Nested
        class TestCase1 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("B", PatternRule.of("1"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("A", TerminalNode.ofUnnamed("0"));
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("B", PatternRule.of("1"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("1"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("B", TerminalNode.ofUnnamed("1"));
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("A", PatternRule.of("1"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("A", TerminalNode.ofUnnamed("0"));
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("A", PatternRule.of("1"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("1"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("A", TerminalNode.ofUnnamed("1"));
            }

        }

        @Nested
        class TestCase5 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("B", PatternRule.of("0"))
                        .shortCircuit();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("A", TerminalNode.ofUnnamed("0"));
            }

        }

        @Nested
        class TestCase6 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("8")))
                        .add("B", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("9")))
                        .shortCircuit();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("018"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("A",
                        TerminalNode.ofUnnamed("0"),
                        TerminalNode.ofUnnamed("1"),
                        TerminalNode.ofUnnamed("8"));
            }

        }

        @Nested
        class TestCase7 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("8")))
                        .add("B", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("9")))
                        .shortCircuit();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("019"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("B",
                        TerminalNode.ofUnnamed("0"),
                        TerminalNode.ofUnnamed("1"),
                        TerminalNode.ofUnnamed("9"));
            }

        }

        @Nested
        class TestCase8 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("8")))
                        .add("B", ChoiceRule.builder()
                                .add(SequenceRule.builder()
                                        .add(PatternRule.of("0"))
                                        .add(PatternRule.of("1"))
                                        .add(PatternRule.of("7")))
                                .add(SequenceRule.builder()
                                        .add(PatternRule.of("0"))
                                        .add(PatternRule.of("1"))
                                        .add(PatternRule.of("9")))
                                .shortCircuit())
                        .shortCircuit();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("019"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("B",
                        TerminalNode.ofUnnamed("0"),
                        TerminalNode.ofUnnamed("1"),
                        TerminalNode.ofUnnamed("9"));
            }

        }

        @Nested
        class TestCase9 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("B")))
                        .hidden()
                        .add("B", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("00"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("A",
                        TerminalNode.ofUnnamed("0"),
                        NonTerminalNode.of("B", TerminalNode.ofUnnamed("0")));
            }

        }

    }

    @Nested
    class EmptyRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", Rule.EMPTY)
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(Reader.nullReader());
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S");
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(Rule.EMPTY)
                                .add(PatternRule.of("0")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("0"));
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(Rule.EMPTY)
                                .add(PatternRule.of("1")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(Reader.nullReader());
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S");
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(ChoiceRule.builder()
                                        .add(PatternRule.of("1"))
                                        .add(Rule.EMPTY))
                                .add(PatternRule.of("0")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("0"));
            }

        }

    }

    @Nested
    class PatternRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("0"));
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of("."))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("0"));
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of(Pattern.compile(".", Pattern.LITERAL)))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("."));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("."));
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of("𝒜"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("𝒜"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("𝒜"));
            }

        }

    }

    @Nested
    class SequenceRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("0"));
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(PatternRule.of("1")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("01"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("0"),
                        TerminalNode.ofUnnamed("1"));
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(SequenceRule.builder()
                                        .add(PatternRule.of("1"))
                                        .add(PatternRule.of("2"))))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("012"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("0"),
                        TerminalNode.ofUnnamed("1"),
                        TerminalNode.ofUnnamed("2"));
            }

        }

    }

    @Nested
    class ChoiceRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("0")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("0"));
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(PatternRule.of("1")))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("1"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("1"));
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("0"))
                                .addEmpty())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(Reader.nullReader());
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S");
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ChoiceRule.builder()
                                        .add(PatternRule.of("1"))
                                        .add(PatternRule.of("2"))))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("2"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("2"));
            }

        }

        @Nested
        class TestCase5 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("."))
                                .shortCircuit())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("1"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("1"));
            }

        }

        @Nested
        class TestCase6 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(SequenceRule.builder()
                                        .add(PatternRule.of("0"))
                                        .add(PatternRule.of("1"))
                                        .add(PatternRule.of("8")))
                                .add(SequenceRule.builder()
                                        .add(PatternRule.of("0"))
                                        .add(PatternRule.of("1"))
                                        .add(PatternRule.of("9")))
                                .shortCircuit())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("019"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("0"),
                        TerminalNode.ofUnnamed("1"),
                        TerminalNode.ofUnnamed("9"));
            }

        }

        @Nested
        class TestCase7 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(SequenceRule.builder()
                                        .add(PatternRule.of("0"))
                                        .add(PatternRule.of("1"))
                                        .add(PatternRule.of("8")))
                                .add(ChoiceRule.builder()
                                        .add(SequenceRule.builder()
                                                .add(PatternRule.of("0"))
                                                .add(PatternRule.of("1"))
                                                .add(PatternRule.of("7")))
                                        .add(SequenceRule.builder()
                                                .add(PatternRule.of("0"))
                                                .add(PatternRule.of("1"))
                                                .add(PatternRule.of("9")))
                                        .shortCircuit())
                                .shortCircuit())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("019"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S",
                        TerminalNode.ofUnnamed("0"),
                        TerminalNode.ofUnnamed("1"),
                        TerminalNode.ofUnnamed("9"));
            }

        }

    }

    @Nested
    class ReferenceRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ReferenceRule.of("A"))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", NonTerminalNode.of("A", TerminalNode.ofUnnamed("0")));
            }

        }

    }

    abstract class QuantifierRuleTestCase {

        abstract Quantifiable createRule();

        abstract String createInput();

        abstract Stream<Tree> expectedSubTree();

        @Nested
        class TestCase1 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().opt())
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader(createInput().repeat(0)));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
                        .limit(0)
                        .flatMap(e -> e)
                        .toArray(Tree[]::new));
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().zeroOrMore())
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader(createInput().repeat(0)));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
                        .limit(0)
                        .flatMap(e -> e)
                        .toArray(Tree[]::new));
            }

        }

        @Nested
        class TestCase3 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().oneOrMore())
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader(createInput().repeat(1)));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
                        .limit(1)
                        .flatMap(e -> e)
                        .toArray(Tree[]::new));
            }

        }

        @Nested
        class TestCase4 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().oneOrMore())
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader(createInput().repeat(2)));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
                        .limit(2)
                        .flatMap(e -> e)
                        .toArray(Tree[]::new));
            }

        }

        @Nested
        class TestCase5 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().exactly(2))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader(createInput().repeat(2)));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
                        .limit(2)
                        .flatMap(e -> e)
                        .toArray(Tree[]::new));
            }

        }

        @Nested
        class TestCase6 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().atLeast(1))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader(createInput().repeat(1)));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
                        .limit(1)
                        .flatMap(e -> e)
                        .toArray(Tree[]::new));
            }

        }

        @Nested
        class TestCase7 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().atLeast(1))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader(createInput().repeat(2)));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
                        .limit(2)
                        .flatMap(e -> e)
                        .toArray(Tree[]::new));
            }

        }

        @Nested
        class TestCase8 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().range(1, 2))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader(createInput().repeat(1)));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
                        .limit(1)
                        .flatMap(e -> e)
                        .toArray(Tree[]::new));
            }

        }

        @Nested
        class TestCase9 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().range(1, 2))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader(createInput().repeat(2)));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
                        .limit(2)
                        .flatMap(e -> e)
                        .toArray(Tree[]::new));
            }

        }

    }

    @Nested
    class QuantifierRuleTestCase1 extends QuantifierRuleTestCase {

        @Override
        Quantifiable createRule() {
            return PatternRule.of("0");
        }

        @Override
        String createInput() {
            return "0";
        }

        @Override
        Stream<Tree> expectedSubTree() {
            return Stream.of(TerminalNode.ofUnnamed("0"));
        }

    }

    @Nested
    class QuantifierRuleTestCase2 extends QuantifierRuleTestCase {

        @Override
        Quantifiable createRule() {
            return SequenceRule.builder()
                    .add(PatternRule.of("0"))
                    .add(PatternRule.of("1"));
        }

        @Override
        String createInput() {
            return "01";
        }

        @Override
        Stream<Tree> expectedSubTree() {
            return Stream.of(
                    TerminalNode.ofUnnamed("0"),
                    TerminalNode.ofUnnamed("1"));
        }

    }

    @Nested
    class QuantifierRuleTestCase3 extends QuantifierRuleTestCase {

        @Override
        Quantifiable createRule() {
            return ChoiceRule.builder()
                    .add(PatternRule.of("0"))
                    .add(PatternRule.of("1"));
        }

        @Override
        String createInput() {
            return "1";
        }

        @Override
        Stream<Tree> expectedSubTree() {
            return Stream.of(TerminalNode.ofUnnamed("1"));
        }

    }

    @Nested
    class QuantifierRuleTestCase4 extends QuantifierRuleTestCase {

        @Override
        Quantifiable createRule() {
            return ReferenceRule.of("A");
        }

        @Override
        String createInput() {
            return "0";
        }

        @Override
        Stream<Tree> expectedSubTree() {
            return Stream.of(NonTerminalNode.of("A", TerminalNode.ofUnnamed("0")));
        }

    }

    @Nested
    class SkipRuleTestCase {

        @Nested
        class TestCase1 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of("0").skip())
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("0"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S");
            }

        }

        @Nested
        class TestCase2 implements ParserTestCase {

            @Override
            public Parser createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0").skip())
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("2").skip()))
                        .build();
                var factory = ParserFactory.of(grammar);
                return factory.createParser(new StringReader("012"));
            }

            @Override
            public Tree expectedTree() {
                return NonTerminalNode.of("S", TerminalNode.ofUnnamed("1"));
            }

        }

    }

}
