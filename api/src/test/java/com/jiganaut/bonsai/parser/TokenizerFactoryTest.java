package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.ProductionSet;

class TokenizerFactoryTest {

    @Test
    @DisplayName("newFactory(Grammar) [Null parameter]")
    void newFactoryInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> TokenizerFactory.of(null));
    }

    @Test
    @DisplayName("newFactory(Grammar)")
    void newFactory() throws Exception {
        var grammar = ProductionSet.builder()
                .add("S", () -> PatternRule.of(""))
                .build();
        var factory = TokenizerFactory.of(grammar);

        assertNotNull(factory);
    }

    @Test
    @DisplayName("loadFactory(st:String) [Null parameter]")
    void loadFactoryStInCaseNullParameter() throws Exception {
        assertThrows(NoSuchElementException.class, () -> TokenizerFactory.load(null));
    }

    @Test
    @DisplayName("loadFactory(st:String) [No such factory]")
    void loadFactoryStInCaseNoSuchFactory() throws Exception {
        assertThrows(NoSuchElementException.class, () -> TokenizerFactory.load(""));
    }
}
