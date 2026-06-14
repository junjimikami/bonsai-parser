package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockGrammar;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

/**
 *
 * @author Junji Mikami
 */
class ParserFactoryTest {

    @Test
    @DisplayName("of(gr:Grammar) [gr == null]")
    void ofWhenGrIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> ParserFactory.of(null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("of(gr:Grammar)")
    void of() throws Exception {
        var factory = ParserFactory.of(mockGrammar());

        assertNotNull(factory);
    }

    @Nested
    class TestCase1 implements ParserFactoryTestCase<String> {

        @Override
        public ParserFactory<String> createTarget() {
            return ParserFactory.of(mockGrammar());
        }

    }

    @Nested
    class TestCase2 implements ParserFactoryTestCase<String> {

        @Override
        public ParserFactory<String> createTarget() {
            return ParserFactory.of(mockGrammar());
        }

    }

    @Nested
    class TestCase3 implements ParserFactoryTestCase<String> {

        @Override
        public ParserFactory<String> createTarget() {
            return ParserFactory.of(mockGrammar());
        }

    }

}
