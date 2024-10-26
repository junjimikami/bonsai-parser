package com.jiganaut.bonsai.grammar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import com.jiganaut.bonsai.grammar.Grammar.Builder;

class GrammarTest {

    @Nested
    class TestCase1 implements GrammarTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn("1");
            when(production.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production);
        }

        @Override
        public Grammar createTarget() {
            var builder = Grammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }

    }

    @Nested
    class TestCase2 implements GrammarTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn("1");
            when(production.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            testData = Stream.of(production, production2)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Grammar createTarget() {
            var builder = Grammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }

    }

    @Nested
    class TestCase3 implements GrammarTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn("1");
            when(production.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            var production3 = mock(Production.class);
            when(production3.getSymbol()).thenReturn("2");
            when(production3.getRule()).thenReturn(mock(Rule.class));
            testData = Stream.of(production, production2, production3)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Grammar createTarget() {
            var builder = Grammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }

    }

    @Nested
    class TestCase4 implements GrammarTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn("1");
            when(production.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            testData = Stream.of(production, production2)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Grammar createTarget() {
            var builder = Grammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            builder.setStartSymbol("2");
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "2";
        }

    }

    @Nested
    class BuilderTestCase1 implements GrammarTestCase.BuilderTestCase {

        @Override
        public Builder createTarget() {
            return Grammar.builder();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return Set.of();
        }

        @Override
        public String expectedStartSymbol() {
            return null;
        }
    }

    @Nested
    class BuilderTestCase2 implements GrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn("1");
            when(production.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production);
        }

        @Override
        public Builder createTarget() {
            var builder = Grammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }
    }

    @Nested
    class BuilderTestCase3 implements GrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            testData = Stream.of(production1, production2)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Builder createTarget() {
            var builder = Grammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }
    }

    @Nested
    class BuilderTestCase4 implements GrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(ReferenceRule.of("NO_SUCH_SYMBOL"));
            testData = Stream.of(production1, production2)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Builder createTarget() {
            var builder = Grammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }
    }

    @Nested
    class BuilderTestCase5 implements GrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(ChoiceRule.class));
            testData = Stream.of(production1, production2)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Builder createTarget() {
            var builder = Grammar.builder();
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

        @Override
        public String expectedStartSymbol() {
            return "1";
        }
    }

    @Nested
    class BuilderTestCase6 implements GrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(SequenceRule.class));
            testData = Stream.of(production1, production2)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Builder createTarget() {
            var builder = Grammar.builder();
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

        @Override
        public String expectedStartSymbol() {
            return "1";
        }
    }

    @Nested
    class BuilderTestCase7 implements GrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(null);
            testData = Stream.of(production1, production2)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Builder createTarget() {
            var builder = Grammar.builder();
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

        @Override
        public String expectedStartSymbol() {
            return "1";
        }
    }

    @Nested
    class BuilderTestCase8 implements GrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            testData = Stream.of(production1, production2)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Builder createTarget() {
            var builder = Grammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            builder.setStartSymbol("2");
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "2";
        }
    }

    @Nested
    class BuilderTestCase9 implements GrammarTestCase.BuilderTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production1 = mock(Production.class);
            when(production1.getSymbol()).thenReturn("1");
            when(production1.getRule()).thenReturn(mock(Rule.class));
            var production2 = mock(Production.class);
            when(production2.getSymbol()).thenReturn("2");
            when(production2.getRule()).thenReturn(mock(Rule.class));
            testData = Stream.of(production1, production2)
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
        }

        @Override
        public Builder createTarget() {
            var builder = Grammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            builder.setStartSymbol("3");
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "3";
        }
    }

}
