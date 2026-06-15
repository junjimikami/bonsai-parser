package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockGrammar;
import static com.jiganaut.bonsai.parser.MockFactory.mockProductionRule;
import static com.jiganaut.bonsai.parser.MockFactory.mockRule;
import static com.jiganaut.bonsai.parser.MockFactory.mockToken;

import java.util.List;

import org.junit.jupiter.api.Nested;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.grammar.Rule;

/**
 *
 * @author Junji Mikami
 */
class ErrorNodeTest {

    @Nested
    class TestCase1 implements ErrorNodeTestCase<String> {

        @Override
        public ErrorNode<String> createTarget() {
            return ErrorNode.<String>builder()
                    .build();
        }

        @Override
        public Position expectedPosition() {
            return Position.UNKNOWN;
        }

        @Override
        public Grammar<String> expectedGrammar() {
            return null;
        }

        @Override
        public List<ProductionRule<String>> expectedProductionPath() {
            return null;
        }

        @Override
        public Rule<String> expectedExpectedRule() {
            return null;
        }

        @Override
        public Token<String> expectedFoundToken() {
            return null;
        }

        @Override
        public String expectedMessage() {
            return null;
        }

    }

    @Nested
    class TestCase2 implements ErrorNodeTestCase<String> {

        Grammar<String> testGrammar = mockGrammar();
        List<ProductionRule<String>> testProductionPath = List.of(mockProductionRule());
        Rule<String> testExpectedRule = mockRule();
        Token<String> testFoundToken = mockToken("name", "value");

        @Override
        public ErrorNode<String> createTarget() {
            return ErrorNode.<String>builder()
                    .setGrammar(testGrammar)
                    .setProductionPath(testProductionPath)
                    .setExpectedRule(testExpectedRule)
                    .setFoundToken(testFoundToken)
                    .setMessage(expectedMessage())
                    .build();
        }

        @Override
        public Position expectedPosition() {
            return testFoundToken.getPosition();
        }

        @Override
        public Grammar<String> expectedGrammar() {
            return testGrammar;
        }

        @Override
        public List<ProductionRule<String>> expectedProductionPath() {
            return testProductionPath;
        }

        @Override
        public Rule<String> expectedExpectedRule() {
            return testExpectedRule;
        }

        @Override
        public Token<String> expectedFoundToken() {
            return testFoundToken;
        }

        @Override
        public String expectedMessage() {
            return "message";
        }

    }

}