package com.unitedjiga.common.parsing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.grammar.Expressions;
import com.unitedjiga.common.parsing.grammar.Grammar;

class TokenizerFactoryTest {

    @Test
    @DisplayName("newFactory(Grammar) [Null parameter]")
    void newFactoryInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> TokenizerFactory.newFactory(null))
                .printStackTrace();
    }

    @Test
    @DisplayName("newFactory(Grammar)")
    void newFactory() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Expressions.pattern(""))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);

        assertNotNull(factory);
    }

}
