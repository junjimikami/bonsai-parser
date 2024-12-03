package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.ChoiceGrammar;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.SingleOriginGrammar;

class ParserFactoryTest {

    @Test
    @DisplayName("of(Grammar) [Null parameter]")
    void ofInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> ParserFactory.of(null));
    }

    @Test
    @DisplayName("of(Grammar)")
    void of() throws Exception {
        var grammar = mock(Grammar.class);
        var factory = ParserFactory.of(grammar);

        assertNotNull(factory);
    }

    @Test
    @DisplayName("load(st:String) [Null parameter]")
    void loadStInCaseNullParameter() throws Exception {
        assertThrows(NoSuchElementException.class, () -> ParserFactory.load((String) null));
    }

    @Test
    @DisplayName("load(st:String) [No such factory]")
    void loadStInCaseNoSuchFactory() throws Exception {
        assertThrows(NoSuchElementException.class, () -> ParserFactory.load(""));
    }

    @Nested
    class TestCase1 implements ParserFactoryTestCase {

        @Override
        public ParserFactory createTarget() {
            var grammar = mock(Grammar.class);
            return ParserFactory.of(grammar);
        }

    }

    @Nested
    class TestCase2 implements ParserFactoryTestCase {

        @Override
        public ParserFactory createTarget() {
            var grammar = mock(ChoiceGrammar.class);
            return ParserFactory.of(grammar);
        }

    }

    @Nested
    class TestCase3 implements ParserFactoryTestCase {

        @Override
        public ParserFactory createTarget() {
            var grammar = mock(SingleOriginGrammar.class);
            return ParserFactory.of(grammar);
        }

    }

}
