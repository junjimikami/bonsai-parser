package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Rules;

class ParserFactoryTest {

    @Test
    @DisplayName("newFactory(Grammar) [Null parameter]")
    void newFactoryInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> ParserFactory.newFactory(null))
                .printStackTrace();
    }

    @Test
    @DisplayName("newFactory(Grammar)")
    void newFactory() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Rules.pattern(""))
                .build();
        var factory = ParserFactory.newFactory(grammar);

        assertNotNull(factory);
    }

    @Test
    @DisplayName("loadFactory(st:String) [Null parameter]")
    void loadFactoryStInCaseNullParameter() throws Exception {
        assertThrows(NoSuchElementException.class, () -> ParserFactory.loadFactory(null));
    }

    @Test
    @DisplayName("loadFactory(st:String) [No such factory]")
    void loadFactoryStInCaseNoSuchFactory() throws Exception {
        assertThrows(NoSuchElementException.class, () -> ParserFactory.loadFactory(""));
    }

    @Test
    @DisplayName("loadFactory(st:String, cl:ClassLoader) [Null parameter]")
    void loadFactoryStClInCaseNullParameter() throws Exception {
        assertThrows(NoSuchElementException.class,
                () -> ParserFactory.loadFactory(null, ClassLoader.getSystemClassLoader()));
    }

    @Test
    @DisplayName("loadFactory(st:String, cl:ClassLoader) [No such factory]")
    void loadFactoryStClInCaseNoSuchFactory() throws Exception {
        assertThrows(NoSuchElementException.class,
                () -> ParserFactory.loadFactory("", ClassLoader.getSystemClassLoader()));
    }
}