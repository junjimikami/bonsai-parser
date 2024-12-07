package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

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

        var grammar = mock(Grammar.class);
        when(grammar.withSymbol(expectedSymbol())).then(invocation -> {
            var p = mock(Production.class);
            when(p.getSymbol()).thenReturn(expectedSymbol());
            var ps = mock(ProductionSet.class);
            when(ps.stream()).thenReturn(Stream.of(p));
            return ps;
        });
        var productionSet = target.lookup(grammar);
        verify(grammar).withSymbol(expectedSymbol());

        assertEquals(expectedSymbol(), productionSet.stream().findFirst().get().getSymbol());
    }

}
