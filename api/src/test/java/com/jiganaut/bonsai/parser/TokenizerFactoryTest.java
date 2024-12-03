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

class TokenizerFactoryTest {

    @Test
    @DisplayName("of(Grammar) [Null parameter]")
    void ofInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> TokenizerFactory.of(null));
    }

    @Test
    @DisplayName("of(Grammar)")
    void of() throws Exception {
        var grammar = mock(Grammar.class);
        var factory = TokenizerFactory.of(grammar);

        assertNotNull(factory);
    }

    @Test
    @DisplayName("load(st:String) [Null parameter]")
    void loadStInCaseNullParameter() throws Exception {
        assertThrows(NoSuchElementException.class, () -> TokenizerFactory.load((String) null));
    }

    @Test
    @DisplayName("load(st:String) [No such factory]")
    void loadStInCaseNoSuchFactory() throws Exception {
        assertThrows(NoSuchElementException.class, () -> TokenizerFactory.load(""));
    }

    @Nested
    class TestCase1 implements TokenizerFactoryTestCase {

        @Override
        public TokenizerFactory createTarget() {
            var grammar = mock(Grammar.class);
            return TokenizerFactory.of(grammar);
        }

    }

    @Nested
    class TestCase2 implements TokenizerFactoryTestCase {

        @Override
        public TokenizerFactory createTarget() {
            var grammar = mock(ChoiceGrammar.class);
            return TokenizerFactory.of(grammar);
        }

    }

    @Nested
    class TestCase3 implements TokenizerFactoryTestCase {

        @Override
        public TokenizerFactory createTarget() {
            var grammar = mock(SingleOriginGrammar.class);
            return TokenizerFactory.of(grammar);
        }

    }

}
