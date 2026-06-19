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
interface TokenizerFactoryTestCase<T, R> extends TestCase {

    @Override
    TokenizerFactory<T, R> createTarget();

    @Test
    @DisplayName("createTokenizer(to:Tokenizer) [to == null]")
    default void createTokenizerToWhenToIsNull(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> target.createTokenizer((Tokenizer<T>) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("createTokenizer(to:Tokenizer)")
    default void createTokenizerTo() throws Exception {
        var target = createTarget();

        assertDoesNotThrow(() -> target.createTokenizer(mockTokenizer()));
    }

}

