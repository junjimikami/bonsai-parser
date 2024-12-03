package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.io.Reader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.TestCase;

interface TokenizerFactoryTestCase extends TestCase {

    @Override
    TokenizerFactory createTarget();

    @Test
    @DisplayName("createTokenizer(to:Tokenizer) [Null parameter]")
    default void createTokenizerToInCaseOfNullParameter() throws Exception {
        var target = createTarget();

        assertThrows(NullPointerException.class, () -> target.createTokenizer((Tokenizer) null));
    }

    @Test
    @DisplayName("createTokenizer(to:Tokenizer)")
    default void createTokenizerTo() throws Exception {
        var target = createTarget();

        var tokenizer = mock(Tokenizer.class);
        assertDoesNotThrow(() -> target.createTokenizer(tokenizer));
    }

    @Test
    @DisplayName("createTokenizer(re:Reader) [Null parameter]")
    default void createTokenizerReInCaseOfNullParameter() throws Exception {
        var target = createTarget();

        assertThrows(NullPointerException.class, () -> target.createTokenizer((Reader) null));
    }

    @Test
    @DisplayName("createTokenizer(re:Reader)")
    default void createTokenizerRe() throws Exception {
        var target = createTarget();

        var reader = Reader.nullReader();
        assertDoesNotThrow(() -> target.createTokenizer(reader));
    }

}
