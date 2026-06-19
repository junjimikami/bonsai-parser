package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.MockFactory.mockProductionRule;
import static com.jiganaut.bonsai.grammar.MockFactory.mockRule;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;

/**
 *
 * @author Junji Mikami
 */
class GrammarTest {

    @Nested
    class TestCase1 implements GrammarTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(mockProductionRule("1", mockRule()));

        @Override
        public Grammar<String> createTarget() {
            var builder = Grammar.<String>builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }

    }

    @Nested
    class TestCase2 implements GrammarTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(
                mockProductionRule("1", mockRule()),
                mockProductionRule("2", mockRule()));

        @Override
        public Grammar<String> createTarget() {
            var builder = Grammar.<String>builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }

    }

    @Nested
    class TestCase3 implements GrammarTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(
                mockProductionRule("1", mockRule()),
                mockProductionRule("2", mockRule()),
                mockProductionRule("3", mockRule()));

        @Override
        public Grammar<String> createTarget() {
            var builder = Grammar.<String>builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }

    }

    @Nested
    class TestCase4 implements GrammarTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(
                mockProductionRule("1", mockRule()),
                mockProductionRule("2", mockRule()),
                mockProductionRule("3", mockRule()));

        @Override
        public Grammar<String> createTarget() {
            var builder = Grammar.<String>builder("1");
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }

        @Override
        public String expectedStartSymbol() {
            return "1";
        }

    }

    @Nested
    class TestCase5 implements GrammarTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(
                mockProductionRule("1", mockRule()),
                mockProductionRule("2", mockRule()),
                mockProductionRule("3", mockRule()));

        @Override
        public Grammar<String> createTarget() {
            var builder = Grammar.<String>builder().asShortCircuit();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder.build();
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }

        @Override
        public boolean expectedShortCircuit() {
            return true;
        }

    }

    @Nested
    class BuilderTestCase1 implements GrammarTestCase.BuilderTestCase<String> {

        @Override
        public Grammar.Builder<String> createTarget() {
            return Grammar.builder();
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return Set.of();
        }
    }

    @Nested
    class BuilderTestCase2 implements GrammarTestCase.BuilderTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(mockProductionRule("1", mockRule()));

        @Override
        public Grammar.Builder<String> createTarget() {
            var builder = Grammar.<String>builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }

    }

    @Nested
    class BuilderTestCase3 implements GrammarTestCase.BuilderTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(
                mockProductionRule("1", mockRule()),
                mockProductionRule("2", mockRule()));

        @Override
        public Grammar.Builder<String> createTarget() {
            var builder = Grammar.<String>builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase4 implements GrammarTestCase.BuilderTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(
                mockProductionRule("1", mockRule()),
                mockProductionRule("2", ReferenceRule.of("NO_SUCH_SYMBOL")));

        @Override
        public Grammar.Builder<String> createTarget() {
            var builder = Grammar.<String>builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase5 implements GrammarTestCase.BuilderTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(
                mockProductionRule("1", mockRule()),
                mockProductionRule("2", ChoiceRule.<String>builder().build()));

        @Override
        public Grammar.Builder<String> createTarget() {
            var builder = Grammar.<String>builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase6 implements GrammarTestCase.BuilderTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(
                mockProductionRule("1", mockRule()),
                mockProductionRule("2", SequenceRule.<String>builder().build()));

        @Override
        public Grammar.Builder<String> createTarget() {
            var builder = Grammar.<String>builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData;
        }
    }

    @Nested
    class BuilderTestCase7 implements GrammarTestCase.BuilderTestCase<String> {

        Set<ProductionRule<String>> testData = Set.of(
                mockProductionRule("1", mockRule()),
                mockProductionRule("2", null));

        @Override
        public Grammar.Builder<String> createTarget() {
            var builder = Grammar.<String>builder();
            testData.forEach(e -> builder.add(e.getSymbol(), e.getRule()));
            return builder;
        }

        @Override
        public Set<ProductionRule<String>> expectedProductionRules() {
            return testData.stream()
                    .filter(e -> e.getRule() != null)
                    .collect(Collectors.toSet());
        }
    }

}
