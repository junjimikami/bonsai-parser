package com.jiganaut.bonsai.grammar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import com.jiganaut.bonsai.grammar.ProductionSet.Builder;

class ProductionSetTest {

    @Nested
    class TestCase1 implements ProductionSetTestCase {

        Set<Production> testData;

        @BeforeEach
        void init() {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn("1");
            when(production.getRule()).thenReturn(mock(Rule.class));
            testData = Set.of(production);
        }

        @Override
        public ProductionSet createTarget() {
            var builder = ProductionSet.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

    }

    @Nested
    class TestCase2 implements ProductionSetTestCase {

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
        public ProductionSet createTarget() {
            var builder = ProductionSet.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

    }

    @Nested
    class TestCase3 implements ProductionSetTestCase {

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
        public ProductionSet createTarget() {
            var builder = ProductionSet.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }

    }

    @Nested
    class BuilderTestCase1 implements ProductionSetTestCase.BuilderTestCase {

        @Override
        public Builder createTarget() {
            return ProductionSet.builder();
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return Set.of();
        }
    }

    @Nested
    class BuilderTestCase2 implements ProductionSetTestCase.BuilderTestCase {

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
            var builder = ProductionSet.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase3 implements ProductionSetTestCase.BuilderTestCase {

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
        public Builder createTarget() {
            var builder = ProductionSet.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase4 implements ProductionSetTestCase.BuilderTestCase {

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
        public Builder createTarget() {
            var builder = ProductionSet.builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<Production> expectedProductionSet() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase5 implements ProductionSetTestCase.BuilderTestCase {

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
        public Builder createTarget() {
            var builder = ProductionSet.builder();
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
    class BuilderTestCase6 implements ProductionSetTestCase.BuilderTestCase {

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
        public Builder createTarget() {
            var builder = ProductionSet.builder();
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
    class BuilderTestCase7 implements ProductionSetTestCase.BuilderTestCase {

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
        public Builder createTarget() {
            var builder = ProductionSet.builder();
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
