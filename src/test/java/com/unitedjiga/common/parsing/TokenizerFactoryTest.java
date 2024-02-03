package com.unitedjiga.common.parsing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.grammar.Rules;
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
                .add("S", Rules.pattern(""))
                .build();
        var factory = TokenizerFactory.newFactory(grammar);

        assertNotNull(factory);
    }

    @Test
    @DisplayName("loadFactory(st:String) [Null parameter]")
    void loadFactoryStInCaseNullParameter() throws Exception {
        assertThrows(NoSuchElementException.class, () -> TokenizerFactory.loadFactory(null));
    }

    @Test
    @DisplayName("loadFactory(st:String) [No such factory]")
    void loadFactoryStInCaseNoSuchFactory() throws Exception {
        assertThrows(NoSuchElementException.class, () -> TokenizerFactory.loadFactory(""));
    }

    @Test
    @DisplayName("loadFactory(st:String, cl:ClassLoader) [Null parameter]")
    void loadFactoryStClInCaseNullParameter() throws Exception {
        assertThrows(NoSuchElementException.class,
                () -> TokenizerFactory.loadFactory(null, ClassLoader.getSystemClassLoader()));
    }

    @Test
    @DisplayName("loadFactory(st:String, cl:ClassLoader) [No such factory]")
    void loadFactoryStClInCaseNoSuchFactory() throws Exception {
        assertThrows(NoSuchElementException.class,
                () -> TokenizerFactory.loadFactory("", ClassLoader.getSystemClassLoader()));
    }
}
