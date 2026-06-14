package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockToken;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
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
class TokenizerTest {

    @Nested
    class ParseExceptionTestCase1 implements ParseExceptionTestCase {

        @Override
        public Executable createTarget(Grammar<String> grammar, Reader reader) {
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            var tokenizer = factory.createTokenizer(TextSource.of(reader));
            return () -> tokenizer.next();
        }

    }

    @Nested
    class TestCase1 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
            var grammar = Grammar.<String>builder("S")
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("A")))
                    .add("A", Rules.pattern("0"))
                    .build();
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("0000")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("S", "00"),
                    mockToken("S", "00")).iterator();
        }

    }

    @Nested
    class TestCase2 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
            var grammar = Grammar.<String>builder("S")
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("A")))
                    .add("S", Rules.pattern("1"))
                    .add("A", Rules.pattern("0"))
                    .build();
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("0000")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("S", "00"),
                    mockToken("S", "00")).iterator();
        }

    }

    @Nested
    class TestCase3 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
            var grammar = Grammar.<String>builder("S")
                    .add("S", SequenceRule.<String>builder()
                            .add(Rules.pattern("0"))
                            .add(ReferenceRule.of("A")))
                    .add("S", Rules.pattern("1"))
                    .add("A", Rules.pattern("0"))
                    .build();
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("100")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("S", "1"),
                    mockToken("S", "00")).iterator();
        }

    }

    @Nested
    class TestCase4 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
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
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("0102")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("S", "01"),
                    mockToken("S", "02")).iterator();
        }

    }

    @Nested
    class TestCase5 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
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
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("0201")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("S", "02"),
                    mockToken("S", "01")).iterator();
        }

    }

    @Nested
    class TestCase6 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
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
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("020301")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("S", "02"),
                    mockToken("S", "03"),
                    mockToken("S", "01")).iterator();
        }

    }

    @Nested
    class TestCase7 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("B", Rules.pattern("1"))
                    .build();
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("01")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("A", "0"),
                    mockToken("B", "1")).iterator();
        }

    }

    @Nested
    class TestCase8 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("B", Rules.pattern("1"))
                    .build();
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("10")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("B", "1"),
                    mockToken("A", "0")).iterator();
        }


    }

    @Nested
    class TestCase9 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("A", Rules.pattern("1"))
                    .build();
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("01")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("A", "0"),
                    mockToken("A", "1")).iterator();
        }

    }

    @Nested
    class TestCase10 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("A", Rules.pattern("1"))
                    .build();
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("10")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("A", "1"),
                    mockToken("A", "0")).iterator();
        }

    }

    @Nested
    class TestCase11 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add("A", Rules.pattern("0"))
                    .add("B", Rules.pattern("0"))
                    .asShortCircuit()
                    .build();
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("00")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("A", "0"),
                    mockToken("A", "0")).iterator();
        }

    }

    @Nested
    class TestCase12 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
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
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("018019")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("A", "018"),
                    mockToken("B", "019")).iterator();
        }

    }

    @Nested
    class TestCase13 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
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
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("019018")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("B", "019"),
                    mockToken("A", "018")).iterator();
        }

    }

    @Nested
    class TestCase14 implements TokenizerTestCase<String> {

        @Override
        public Tokenizer<String> createTarget() {
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
            var factory = TokenizerFactory.of(grammar, Collectors.joining());
            return factory.createTokenizer(TextSource.of(new StringReader("019017018")));
        }

        @Override
        public Iterator<Token<String>> expectedIterator() {
            return List.of(
                    mockToken("B", "019"),
                    mockToken("B", "017"),
                    mockToken("A", "018")).iterator();
        }

    }

    @Nested
    class EmptyRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.empty())
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(Reader.nullReader()));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.<Token<String>>of().iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.empty())
                                .add(Rules.pattern("0")))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("00")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "0"),
                        mockToken("S", "0")).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.empty())
                                .add(Rules.pattern("1")))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(Reader.nullReader()));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.<Token<String>>of().iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(ChoiceRule.<String>builder()
                                        .add(Rules.pattern("1"))
                                        .add(Rules.empty()))
                                .add(Rules.pattern("0")))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("010")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "0"),
                        mockToken("S", "10")).iterator();
            }

        }

    }

    @Nested
    class MatchingRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("00")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "0"),
                        mockToken("S", "0")).iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern("."))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("01")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "0"),
                        mockToken("S", "1")).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern(Pattern.compile(".", Pattern.LITERAL)))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("..")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "."),
                        mockToken("S", ".")).iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern("𝒜"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("𝒜𝒜")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "𝒜"),
                        mockToken("S", "𝒜")).iterator();
            }

        }

    }

    @Nested
    class SequenceRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.pattern("0")))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("00")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "0"),
                        mockToken("S", "0")).iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .add(Rules.pattern("1")))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("0101")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "01"),
                        mockToken("S", "01")).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .add(SequenceRule.<String>builder()
                                        .add(Rules.pattern("1"))
                                        .add(Rules.pattern("2"))))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("012012")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "012"),
                        mockToken("S", "012")).iterator();
            }

        }

    }

    @Nested
    class ChoiceRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("0")))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("00")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "0"),
                        mockToken("S", "0")).iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .add(Rules.pattern("1")))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("10")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "1"),
                        mockToken("S", "0")).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .addEmpty())
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(Reader.nullReader()));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.<Token<String>>of().iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("0"))
                                .add(ChoiceRule.<String>builder()
                                        .add(Rules.pattern("1"))
                                        .add(Rules.pattern("2"))))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("201")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "2"),
                        mockToken("S", "0"),
                        mockToken("S", "1")).iterator();
            }

        }

        @Nested
        class TestCase5 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ChoiceRule.<String>builder()
                                .add(Rules.pattern("1"))
                                .add(Rules.pattern("."))
                                .asShortCircuit())
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("12")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "1"),
                        mockToken("S", "2")).iterator();
            }

        }

        @Nested
        class TestCase6 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
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
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("019018")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "019"),
                        mockToken("S", "018")).iterator();
            }

        }

        @Nested
        class TestCase7 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
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
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("019017018")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "019"),
                        mockToken("S", "017"),
                        mockToken("S", "018")).iterator();
            }

        }

    }

    @Nested
    class ReferenceRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", ReferenceRule.of("A"))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("00")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(
                        mockToken("S", "0"),
                        mockToken("S", "0")).iterator();
            }

        }

    }

    abstract class TokenizerTestCaseForQuantifierRule {

        abstract Quantifiable<String> createRule();

        abstract String expectedValue();

        @Nested
        class TestCase1 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().opt())
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader(expectedValue().repeat(0))));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.<Token<String>>of().iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().zeroOrMore())
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader(expectedValue().repeat(0))));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.<Token<String>>of().iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().oneOrMore())
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader(expectedValue().repeat(1))));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(mockToken("S", expectedValue().repeat(1))).iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().oneOrMore())
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader(expectedValue().repeat(2))));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(mockToken("S", expectedValue().repeat(2))).iterator();
            }

        }

        @Nested
        class TestCase5 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().exactly(2))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader(expectedValue().repeat(2))));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(mockToken("S", expectedValue().repeat(2))).iterator();
            }

        }

        @Nested
        class TestCase6 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().atLeast(1))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader(expectedValue().repeat(1))));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(mockToken("S", expectedValue().repeat(1))).iterator();
            }

        }

        @Nested
        class TestCase7 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().atLeast(1))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader(expectedValue().repeat(2))));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(mockToken("S", expectedValue().repeat(2))).iterator();
            }

        }

        @Nested
        class TestCase8 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().range(1, 2))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader(expectedValue().repeat(1))));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(mockToken("S", expectedValue().repeat(1))).iterator();
            }

        }

        @Nested
        class TestCase9 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", createRule().range(1, 2))
                        .add("A", Rules.pattern("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader(expectedValue().repeat(2))));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(mockToken("S", expectedValue().repeat(2))).iterator();
            }

        }

    }

    @Nested
    class QuantifierRuleTestCase {

        @Nested
        class QuantifierRuleTestCase1 extends TokenizerTestCaseForQuantifierRule {

            @Override
            Quantifiable<String> createRule() {
                return Rules.pattern("0");
            }

            @Override
            String expectedValue() {
                return "0";
            }

        }

        @Nested
        class QuantifierRuleTestCase2 extends TokenizerTestCaseForQuantifierRule {

            @Override
            Quantifiable<String> createRule() {
                return SequenceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(Rules.pattern("1"))
                        .build();
            }

            @Override
            String expectedValue() {
                return "01";
            }

        }

        @Nested
        class QuantifierRuleTestCase3 extends TokenizerTestCaseForQuantifierRule {

            @Override
            Quantifiable<String> createRule() {
                return ChoiceRule.<String>builder()
                        .add(Rules.pattern("0"))
                        .add(Rules.pattern("1"))
                        .build();
            }

            @Override
            String expectedValue() {
                return "1";
            }

        }

        @Nested
        class QuantifierRuleTestCase4 extends TokenizerTestCaseForQuantifierRule {

            @Override
            Quantifiable<String> createRule() {
                return ReferenceRule.<String>of("A");
            }

            @Override
            String expectedValue() {
                return "0";
            }

        }

    }

    @Nested
    class SkipRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", Rules.pattern("0").skip())
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("0")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.<Token<String>>of().iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase<String> {

            @Override
            public Tokenizer<String> createTarget() {
                var grammar = Grammar.<String>builder("S")
                        .add("S", SequenceRule.<String>builder()
                                .add(Rules.pattern("0").skip())
                                .add(Rules.pattern("1"))
                                .add(Rules.pattern("2").skip()))
                        .build();
                var factory = TokenizerFactory.of(grammar, Collectors.joining());
                return factory.createTokenizer(TextSource.of(new StringReader("012")));
            }

            @Override
            public Iterator<Token<String>> expectedIterator() {
                return List.of(mockToken("S", "1")).iterator();
            }

        }

    }

}
