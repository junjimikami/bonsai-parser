package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockGrammar;
import static com.jiganaut.bonsai.parser.MockFactory.mockRule;
import static com.jiganaut.bonsai.parser.MockFactory.mockToken;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.parser.Tree.Kind;

/**
 *
 * @author Junji Mikami
 */
interface ErrorNodeTestCase<T> extends TreeTestCase<T> {

    interface BuilderTestCase<T> extends TreeTestCase.BuilderTestCase<T> {

        @Override
        ErrorNode.Builder<T> createTarget();

        @Override
        ErrorNode<T> expectedTree();

        @Override
        default boolean canBuild() {
            return true;
        }

        @Test
        @DisplayName("setGrammar(Grammar)")
        default void setGrammar(Grammar<T> grammar) throws Exception {
            var target = createTarget();

            assertEquals(target, target.setGrammar(null));
            assertEquals(target, target.setGrammar(mockGrammar()));
        }

        @Test
        @DisplayName("setProductionPath(List<ProductionRule>)")
        default void setProductionPath() throws Exception {
            var target = createTarget();

            assertEquals(target, target.setProductionPath(null));
            assertEquals(target, target.setProductionPath(List.of()));
        }

        @Test
        @DisplayName("setExpectedRule(Rule)")
        default void setExpectedRule() throws Exception {
            var target = createTarget();

            assertEquals(target, target.setExpectedRule(null));
            assertEquals(target, target.setExpectedRule(mockRule()));
        }

        @Test
        @DisplayName("setFoundToken(Token)")
        default void setFoundToken() throws Exception {
            var target = createTarget();

            assertEquals(target, target.setFoundToken(null));
            assertEquals(target, target.setFoundToken(mockToken()));
        }

        @Test
        @DisplayName("setMessage(String)")
        default void setMessage() throws Exception {
            var target = createTarget();

            assertEquals(target, target.setMessage(null));
            assertEquals(target, target.setMessage("message"));
        }

    }

    @Override
    ErrorNode<T> createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.ERROR;
    }

    @Override
    default List<Tree<T>> expectedSubTrees() {
        if (expectedFoundToken() == null) {
            return List.of();
        }
        return List.of(expectedFoundToken());
    }

    @Override
    default List<T> expectedValues() {
        if (expectedFoundToken() == null) {
            return List.of();
        }
        return List.of(expectedFoundToken().getValue());
    }

    Grammar<T> expectedGrammar();
    List<ProductionRule<T>> expectedProductionPath();
    Rule<T> expectedExpectedRule();
    Token<T> expectedFoundToken();
    String expectedMessage();

    @Test
    @DisplayName("getGrammar()")
    default void getGrammar() throws Exception {
        var target = createTarget();

        assertEquals(expectedGrammar(), target.getGrammar());
    }

    @Test
    @DisplayName("getProductionPath()")
    default void getProductionPath() throws Exception {
        var target = createTarget();

        assertEquals(expectedProductionPath(), target.getProductionPath());
    }

    @Test
    @DisplayName("getExpectedRule()")
    default void getExpectedRule() throws Exception {
        var target = createTarget();

        assertEquals(expectedExpectedRule(), target.getExpectedRule());
    }

    @Test
    @DisplayName("getFoundToken()")
    default void getFoundToken() throws Exception {
        var target = createTarget();

        assertEquals(expectedFoundToken(), target.getFoundToken());
    }

    @Test
    @DisplayName("getMessage()")
    default void getMessage() throws Exception {
        var target = createTarget();

        assertEquals(expectedMessage(), target.getMessage());
    }

}

