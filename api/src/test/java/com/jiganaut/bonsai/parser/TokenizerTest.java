package com.jiganaut.bonsai.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Nested;
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

class TokenizerTest {

    @Nested
    class ParseExceptionTestCase1 implements ParseExceptionTestCase {

        @Override
        public Executable createTarget(Grammar grammar, Reader reader) {
            var factory = TokenizerFactory.of(grammar);
            var tokenizer = factory.createTokenizer(reader);
            return () -> tokenizer.next();
        }

    }

    @Nested
    class SingleOriginGrammarTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("A")))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("0000"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "00"),
                        createToken("S", "00")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(2L, 4L).iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("A")))
                        .add("S", PatternRule.of("1"))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("0000"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "00"),
                        createToken("S", "00")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(2L, 4L).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("A")))
                        .add("S", PatternRule.of("1"))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("100"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "1"),
                        createToken("S", "00")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 3L).iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
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
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("0102"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "01"),
                        createToken("S", "02")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(2L, 4L).iterator();
            }

        }

        @Nested
        class TestCase5 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
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
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("0201"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "02"),
                        createToken("S", "01")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(2L, 4L).iterator();
            }

        }

        @Nested
        class TestCase6 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
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
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("020301"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "02"),
                        createToken("S", "03"),
                        createToken("S", "01")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(2L, 4L, 6L).iterator();
            }

        }

    }

    @Nested
    class ChoiceGrammarTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("B", PatternRule.of("1"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("01"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("A", "0"),
                        createToken("B", "1")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("B", PatternRule.of("1"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("10"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("B", "1"),
                        createToken("A", "0")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("A", PatternRule.of("1"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("01"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("A", "0"),
                        createToken("A", "1")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("A", PatternRule.of("1"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("10"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("A", "1"),
                        createToken("A", "0")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase5 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", PatternRule.of("0"))
                        .add("B", PatternRule.of("0"))
                        .shortCircuit();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("00"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("A", "0"),
                        createToken("A", "0")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase6 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
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
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("018019"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("A", "018"),
                        createToken("B", "019")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(3L, 6L).iterator();
            }

        }

        @Nested
        class TestCase7 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
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
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("019018"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("B", "019"),
                        createToken("A", "018")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(3L, 6L).iterator();
            }

        }

        @Nested
        class TestCase8 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
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
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("019017018"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("B", "019"),
                        createToken("B", "017"),
                        createToken("A", "018")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(3L, 6L, 9L).iterator();
            }

        }

        @Nested
        class TestCase9 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = ChoiceGrammar.builder()
                        .add("A", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ReferenceRule.of("B")))
                        .hidden()
                        .add("B", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("0000"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("A", "00"),
                        createToken("A", "00")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(2L, 4L).iterator();
            }

        }

    }

    @Nested
    class EmptyRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", Rule.EMPTY)
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(Reader.nullReader());
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.<Token>of().iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.<Long>of().iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.<Long>of().iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(Rule.EMPTY)
                                .add(PatternRule.of("0")))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("00"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "0"),
                        createToken("S", "0")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(Rule.EMPTY)
                                .add(PatternRule.of("1")))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(Reader.nullReader());
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.<Token>of().iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.<Long>of().iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.<Long>of().iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(ChoiceRule.builder()
                                        .add(PatternRule.of("1"))
                                        .add(Rule.EMPTY))
                                .add(PatternRule.of("0")))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("010"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "0"),
                        createToken("S", "10")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 3L).iterator();
            }

        }

    }

    @Nested
    class PatternRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("00"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "0"),
                        createToken("S", "0")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of("."))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("01"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "0"),
                        createToken("S", "1")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of(Pattern.compile(".", Pattern.LITERAL)))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(".."));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "."),
                        createToken("S", ".")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of("𝒜"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("𝒜𝒜"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "𝒜"),
                        createToken("S", "𝒜")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(2L, 4L).iterator();
            }

        }

    }

    @Nested
    class SequenceRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0")))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("00"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "0"),
                        createToken("S", "0")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(PatternRule.of("1")))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("0101"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "01"),
                        createToken("S", "01")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(2L, 4L).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(SequenceRule.builder()
                                        .add(PatternRule.of("1"))
                                        .add(PatternRule.of("2"))))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("012012"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "012"),
                        createToken("S", "012")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(3L, 6L).iterator();
            }

        }

    }

    @Nested
    class ChoiceRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("0")))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("00"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "0"),
                        createToken("S", "0")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(PatternRule.of("1")))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("10"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "1"),
                        createToken("S", "0")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("0"))
                                .addEmpty())
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(Reader.nullReader());
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.<Token>of().iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.<Long>of().iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.<Long>of().iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("0"))
                                .add(ChoiceRule.builder()
                                        .add(PatternRule.of("1"))
                                        .add(PatternRule.of("2"))))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("201"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "2"),
                        createToken("S", "0"),
                        createToken("S", "1")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L, 3L).iterator();
            }

        }

        @Nested
        class TestCase5 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ChoiceRule.builder()
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("."))
                                .shortCircuit())
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("12"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "1"),
                        createToken("S", "2")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

        @Nested
        class TestCase6 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
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
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("019018"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "019"),
                        createToken("S", "018")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(3L, 6L).iterator();
            }

        }

        @Nested
        class TestCase7 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
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
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("019017018"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "019"),
                        createToken("S", "017"),
                        createToken("S", "018")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(3L, 6L, 9L).iterator();
            }

        }

    }

    @Nested
    class ReferenceRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", ReferenceRule.of("A"))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("00"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(
                        createToken("S", "0"),
                        createToken("S", "0")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L, 1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(1L, 2L).iterator();
            }

        }

    }

    abstract class QuantifierRuleTestCase {

        abstract Quantifiable createRule();

        abstract String createInput();

//        abstract Stream<Tree> expectedSubTree();
        abstract String expectedValue();

        @Nested
        class TestCase1 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().opt())
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(createInput().repeat(0)));
            }

//            @Override
//            public Tree expectedTree() {
//                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
//                        .limit(0)
//                        .flatMap(e -> e)
//                        .toArray(Tree[]::new));
//            }
            @Override
            public Iterator<Token> expectedIterator() {
                return List.<Token>of().iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.<Long>of().iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.<Long>of().iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().zeroOrMore())
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(createInput().repeat(0)));
            }

//            @Override
//            public Tree expectedTree() {
//                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
//                        .limit(0)
//                        .flatMap(e -> e)
//                        .toArray(Tree[]::new));
//            }
            @Override
            public Iterator<Token> expectedIterator() {
                return List.<Token>of().iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.<Long>of().iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.<Long>of().iterator();
            }

        }

        @Nested
        class TestCase3 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().oneOrMore())
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(createInput().repeat(1)));
            }

//            @Override
//            public Tree expectedTree() {
//                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
//                        .limit(1)
//                        .flatMap(e -> e)
//                        .toArray(Tree[]::new));
//            }
            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(createToken("S", expectedValue().repeat(1))).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of((long) expectedValue().repeat(1).length()).iterator();
            }

        }

        @Nested
        class TestCase4 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().oneOrMore())
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(createInput().repeat(2)));
            }

//            @Override
//            public Tree expectedTree() {
//                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
//                        .limit(2)
//                        .flatMap(e -> e)
//                        .toArray(Tree[]::new));
//            }
            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(createToken("S", expectedValue().repeat(2))).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of((long) expectedValue().repeat(2).length()).iterator();
            }

        }

        @Nested
        class TestCase5 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().exactly(2))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(createInput().repeat(2)));
            }

//            @Override
//            public Tree expectedTree() {
//                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
//                        .limit(2)
//                        .flatMap(e -> e)
//                        .toArray(Tree[]::new));
//            }
            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(createToken("S", expectedValue().repeat(2))).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of((long) expectedValue().repeat(2).length()).iterator();
            }

        }

        @Nested
        class TestCase6 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().atLeast(1))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(createInput().repeat(1)));
            }

//            @Override
//            public Tree expectedTree() {
//                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
//                        .limit(1)
//                        .flatMap(e -> e)
//                        .toArray(Tree[]::new));
//            }
            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(createToken("S", expectedValue().repeat(1))).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of((long) expectedValue().repeat(1).length()).iterator();
            }

        }

        @Nested
        class TestCase7 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().atLeast(1))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(createInput().repeat(2)));
            }

//            @Override
//            public Tree expectedTree() {
//                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
//                        .limit(2)
//                        .flatMap(e -> e)
//                        .toArray(Tree[]::new));
//            }
            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(createToken("S", expectedValue().repeat(2))).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of((long) expectedValue().repeat(2).length()).iterator();
            }

        }

        @Nested
        class TestCase8 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().range(1, 2))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(createInput().repeat(1)));
            }

//            @Override
//            public Tree expectedTree() {
//                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
//                        .limit(1)
//                        .flatMap(e -> e)
//                        .toArray(Tree[]::new));
//            }
            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(createToken("S", expectedValue().repeat(1))).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of((long) expectedValue().repeat(1).length()).iterator();
            }

        }

        @Nested
        class TestCase9 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", createRule().range(1, 2))
                        .add("A", PatternRule.of("0"))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader(createInput().repeat(2)));
            }

//            @Override
//            public Tree expectedTree() {
//                return NonTerminalNode.of("S", Stream.generate(() -> expectedSubTree())
//                        .limit(2)
//                        .flatMap(e -> e)
//                        .toArray(Tree[]::new));
//            }
            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(createToken("S", expectedValue().repeat(2))).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of((long) expectedValue().repeat(2).length()).iterator();
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
        String expectedValue() {
            return "0";
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
        String expectedValue() {
            return "01";
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
        String expectedValue() {
            return "1";
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
        String expectedValue() {
            return "0";
        }

    }

    @Nested
    class SkipRuleTestCase {

        @Nested
        class TestCase1 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", PatternRule.of("0").skip())
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("0"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.<Token>of().iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.<Long>of().iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.<Long>of().iterator();
            }

        }

        @Nested
        class TestCase2 implements TokenizerTestCase {

            @Override
            public Tokenizer createTarget() {
                var grammar = SingleOriginGrammar.builder()
                        .add("S", SequenceRule.builder()
                                .add(PatternRule.of("0").skip())
                                .add(PatternRule.of("1"))
                                .add(PatternRule.of("2").skip()))
                        .build();
                var factory = TokenizerFactory.of(grammar);
                return factory.createTokenizer(new StringReader("012"));
            }

            @Override
            public Iterator<Token> expectedIterator() {
                return List.of(createToken("S", "1")).iterator();
            }

            @Override
            public Iterator<Long> expectedLineNumbers() {
                return List.of(1L).iterator();
            }

            @Override
            public Iterator<Long> expectedIndexes() {
                return List.of(3L).iterator();
            }

        }

    }

}
