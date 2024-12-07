package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.ServiceConfigurationError;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.grammar.ChoiceGrammar;
import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.SingleOriginGrammar;

class ParserFactoryTest {

    @Test
    @DisplayName("of(Grammar) [Null parameter]")
    void ofInCaseNullParameter(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> ParserFactory.of(null));
        testReporter.publishEntry(ex.getMessage());
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
    void loadStInCaseNullParameter(TestReporter testReporter) throws Exception {
        var ex = assertThrows(ServiceConfigurationError.class, () -> ParserFactory.load((String) null));
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("load(st:String) [No such factory]")
    void loadStInCaseNoSuchFactory(TestReporter testReporter) throws Exception {
        var ex = assertThrows(ServiceConfigurationError.class, () -> ParserFactory.load(""));
        testReporter.publishEntry(ex.getMessage());
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
