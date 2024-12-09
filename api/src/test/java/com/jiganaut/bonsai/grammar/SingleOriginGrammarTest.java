package com.jiganaut.bonsai.grammar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import com.jiganaut.bonsai.grammar.SingleOriginGrammar.Builder;

/**
 * 
 * @author Junji Mikami
 */
class SingleOriginGrammarTest {

    @Nested
    class TestCase1 implements SingleOriginGrammarTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn("1");
            when(production.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production);
        }

        @Override
        public SingleOriginGrammar createTarget() {
            var builder = SingleOriginGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData.stream()
                    .filter(e -> expectedStartSymbol().equals(e.getSymbol()))
                    .collect(Collectors.toSet());
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }

        @Override
        public Set<Production> expectedSet() {
            return testData;
        }

    }

    @Nested
    class TestCase2 implements SingleOriginGrammarTestCase {

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
        public SingleOriginGrammar createTarget() {
            var builder = SingleOriginGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData.stream()
                    .filter(e -> expectedStartSymbol().equals(e.getSymbol()))
                    .collect(Collectors.toSet());
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }

        @Override
        public Set<Production> expectedSet() {
            return testData;
        }

    }

    @Nested
    class TestCase3 implements SingleOriginGrammarTestCase {

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
        public SingleOriginGrammar createTarget() {
            var builder = SingleOriginGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData.stream()
                    .filter(e -> expectedStartSymbol().equals(e.getSymbol()))
                    .collect(Collectors.toSet());
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }

        @Override
        public Set<Production> expectedSet() {
            return testData;
        }

    }

    @Nested
    class TestCase4 implements SingleOriginGrammarTestCase {

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
        public SingleOriginGrammar createTarget() {
            var builder = SingleOriginGrammar.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            builder.setStartSymbol("2");
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData.stream()
                    .filter(e -> expectedStartSymbol().equals(e.getSymbol()))
                    .collect(Collectors.toSet());
        }

        @Override
        public String expectedStartSymbol() {
            return "2";
        }

        @Override
        public Set<Production> expectedSet() {
            return testData;
        }

    }

    @Nested
    class BuilderTestCase1 implements SingleOriginGrammarTestCase.BuilderTestCase {

        @Override
        public Builder createTarget() {
            return SingleOriginGrammar.builder();
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
    class BuilderTestCase2 implements SingleOriginGrammarTestCase.BuilderTestCase {

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
            var builder = SingleOriginGrammar.builder();
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
    class BuilderTestCase3 implements SingleOriginGrammarTestCase.BuilderTestCase {

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
            var builder = SingleOriginGrammar.builder();
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
    class BuilderTestCase4 implements SingleOriginGrammarTestCase.BuilderTestCase {

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
            var builder = SingleOriginGrammar.builder();
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
    class BuilderTestCase5 implements SingleOriginGrammarTestCase.BuilderTestCase {

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
            var builder = SingleOriginGrammar.builder();
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
    class BuilderTestCase6 implements SingleOriginGrammarTestCase.BuilderTestCase {

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
            var builder = SingleOriginGrammar.builder();
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
    class BuilderTestCase7 implements SingleOriginGrammarTestCase.BuilderTestCase {

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
            var builder = SingleOriginGrammar.builder();
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
    class BuilderTestCase8 implements SingleOriginGrammarTestCase.BuilderTestCase {

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
            var builder = SingleOriginGrammar.builder();
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
    class BuilderTestCase9 implements SingleOriginGrammarTestCase.BuilderTestCase {

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
            var builder = SingleOriginGrammar.builder();
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
