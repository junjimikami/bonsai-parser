package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

interface ReferenceRuleTestCase extends RuleTestCase, QuantifiableTestCase {

    @Override
    ReferenceRule createTarget();

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

        var productionSet = mock(ProductionSet.class);
        when(productionSet.getProduction(expectedSymbol())).then(invocation -> {
            var production = mock(Production.class);
            when(production.getSymbol()).thenReturn(expectedSymbol());
            return production;
        });
        var production = target.lookup(productionSet);
        verify(productionSet).getProduction(expectedSymbol());

        assertEquals(expectedSymbol(), production.getSymbol());
    }

}
