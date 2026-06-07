package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.GrammarMockFactory.mockRule;

import org.junit.jupiter.api.Nested;

/**
 *
 * @author Junji Mikami
 */
class ProductionRuleTest {

    @Nested
    class TestCase1 implements ProductionRuleTestCase<String> {

        Rule<String> testData = mockRule();

        @Override
        public ProductionRule<String> createTarget() {
            var grammar = Grammar.<String>builder()
                    .add(expectedSymbol(), expectedRule())
                    .build();
            return grammar.getProductionRules().iterator().next();
        }

        @Override
        public String expectedSymbol() {
            return "NAME";
        }

        @Override
        public Rule<String> expectedRule() {
            return testData;
        }

    }

}
