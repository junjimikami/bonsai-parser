package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockGrammar;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

/**
 *
 * @author Junji Mikami
 */
class TokenizerFactoryTest {

    @Test
    @DisplayName("of(gr:Grammar) [gr == null]")
    void ofWhenGrIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> TokenizerFactory.of(null, Collectors.joining()));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("of(gr:Grammar)")
    void of() throws Exception {
        var factory = TokenizerFactory.of(mockGrammar(), Collectors.joining());

        assertNotNull(factory);
    }

    @Nested
    class TestCase1 implements TokenizerFactoryTestCase<String, String> {

        @Override
        public TokenizerFactory<String, String> createTarget() {
            return TokenizerFactory.of(mockGrammar(), Collectors.joining());
        }

    }

    @Nested
    class TestCase2 implements TokenizerFactoryTestCase<String, String> {

        @Override
        public TokenizerFactory<String, String> createTarget() {
            return TokenizerFactory.of(mockGrammar(), Collectors.joining());
        }

    }

    @Nested
    class TestCase3 implements TokenizerFactoryTestCase<String, String> {

        @Override
        public TokenizerFactory<String, String> createTarget() {
            return TokenizerFactory.of(mockGrammar(), Collectors.joining());
        }

    }

}
