package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.GrammarMockFactory.mockGrammar;
import static com.jiganaut.bonsai.grammar.GrammarMockFactory.mockProductionRule;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

/**
 *
 * @author Junji Mikami
 */
interface ReferenceRuleTestCase<T> extends QuantifiableTestCase<T> {

    @Override
    ReferenceRule<T> createTarget();

    @Override
    default Kind expectedKind() {
        return Kind.REFERENCE;
    }

    String expectedSymbol();

    @Test
    @DisplayName("getSymbol")
    default void getSymbol() throws Exception {
        var target = createTarget();

        assertEquals(expectedSymbol(), target.getSymbol());
    }

    @Test
    @DisplayName("lookup")
    default void lookup() throws Exception {
        var target = createTarget();

        Grammar<T> grammar = mockGrammar();
        when(grammar.getProductionRules()).then(invocation -> {
            return Set.of(mockProductionRule(expectedSymbol()));
        });
        var productionSet = target.lookup(grammar);
        var production = productionSet.getChoices().stream()
                .map(e -> (ProductionRule<?>) e)
                .findFirst()
                .orElseThrow();

        assertEquals(expectedSymbol(), production.getSymbol());
    }

}
