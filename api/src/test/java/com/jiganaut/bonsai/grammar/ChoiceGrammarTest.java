package com.jiganaut.bonsai.grammar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

/**
 * 
 * @author Junji Mikami
 */
class ChoiceGrammarTest {

    @Nested
    class TestCase1 implements ChoiceGrammarTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn("1");
            when(production.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production);
        }

        @Override
        public ChoiceGrammar createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public Set<Production> expectedSet() {
            return testData;
        }

        @Override
        public Set<String> expectedHiddenSymbols() {
            return Set.of();
        }

    }

    @Nested
    class TestCase2 implements ChoiceGrammarTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production1, production2);
        }

        @Override
        public ChoiceGrammar createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public Set<Production> expectedSet() {
            return testData;
        }

        @Override
        public Set<String> expectedHiddenSymbols() {
            return Set.of();
        }

    }

    @Nested
    class TestCase3 implements ChoiceGrammarTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            var production3 = mock(Production.class);
            when(production3.getSymbol()).thenReturn("2");
            when(production3.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production1, production2, production3);
        }

        @Override
        public ChoiceGrammar createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public Set<Production> expectedSet() {
            return testData;
        }

        @Override
        public Set<String> expectedHiddenSymbols() {
            return Set.of();
        }

    }

    @Nested
    class TestCase4 implements ChoiceGrammarTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            var production3 = mock(Production.class);
            when(production3.getSymbol()).thenReturn("3");
            when(production3.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production1, production2, production3);
        }

        @Override
        public ChoiceGrammar createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.stream()
                    .filter(e -> e.getSymbol().equals("1"))
                    .forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            builder.hidden();
            testData.stream()
                    .filter(e -> !e.getSymbol().equals("1"))
                    .forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData.stream()
                    .filter(e -> e.getSymbol().equals("1"))
                    .collect(Collectors.toSet());
        }

        @Override
        public Set<Production> expectedSet() {
            return testData;
        }

        @Override
        public Set<String> expectedHiddenSymbols() {
            return testData.stream()
                    .filter(e -> !e.getSymbol().equals("1"))
                    .map(e -> e.getSymbol())
                    .collect(Collectors.toSet());
        }

    }

    @Nested
    class BuilderTestCase1 implements ChoiceGrammarTestCase.BuilderTestCase {

        @Override
        public ChoiceGrammar.Builder createTarget() {
            return ChoiceGrammar.builder();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return Set.of();
        }
    }

    @Nested
    class BuilderTestCase2 implements ChoiceGrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn("1");
            when(production.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production);
        }

        @Override
        public ChoiceGrammar.Builder createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase3 implements ChoiceGrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production1, production2);
        }

        @Override
        public ChoiceGrammar.Builder createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase4 implements ChoiceGrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(ReferenceRule.of("NO_SUCH_SYMBOL"));
            testData = Set.of(production1, production2);
        }

        @Override
        public ChoiceGrammar.Builder createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase5 implements ChoiceGrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(ChoiceRule.class));
            testData = Set.of(production1, production2);
        }

        @Override
        public ChoiceGrammar.Builder createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.stream()
                    .filter(e -> e.getSymbol().equals("1"))
                    .forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            testData.stream()
                    .filter(e -> e.getSymbol().equals("2"))
                    .forEach(e -> builder.add(e.getSymbol(), ChoiceRule.builder()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase6 implements ChoiceGrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(SequenceRule.class));
            testData = Set.of(production1, production2);
        }

        @Override
        public ChoiceGrammar.Builder createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.stream()
                    .filter(e -> e.getSymbol().equals("1"))
                    .forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            testData.stream()
                    .filter(e -> e.getSymbol().equals("2"))
                    .forEach(e -> builder.add(e.getSymbol(), SequenceRule.builder()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase7 implements ChoiceGrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(null);
            testData = Set.of(production1, production2);
        }

        @Override
        public ChoiceGrammar.Builder createTarget() {
            var builder = ChoiceGrammar.builder();
            testData.stream()
                    .filter(e -> e.getSymbol().equals("1"))
                    .forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            testData.stream()
                    .filter(e -> e.getSymbol().equals("2"))
                    .forEach(e -> builder.add(e.getSymbol(), mock(Rule.Builder.class)));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }
    }

}
