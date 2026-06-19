package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockTokenizer;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.TestCase;

/**
 *
 * @author Junji Mikami
 */
interface ParserFactoryTestCase<T> extends TestCase {

    @Override
    ParserFactory<T> createTarget();

    @Test
    @DisplayName("createParser(to:Tokenizer) [to == null]")
    default void createParserToWhenToIsNull(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> target.createParser((Tokenizer<T>) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("createParser(to:Tokenizer)")
    default void createParserTo() throws Exception {
        var target = createTarget();

        assertDoesNotThrow(() -> target.createParser(mockTokenizer()));
    }

}

