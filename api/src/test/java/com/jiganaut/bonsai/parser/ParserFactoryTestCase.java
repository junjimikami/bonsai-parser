package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.io.Reader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.TestCase;

/**
 * 
 * @author Junji Mikami
 */
interface ParserFactoryTestCase extends TestCase {

    @Override
    ParserFactory createTarget();

    @Test
    @DisplayName("createParser(to:Tokenizer) [Null parameter]")
    default void createParserToInCaseOfNullParameter(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> target.createParser((Tokenizer) null));
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("createParser(to:Tokenizer)")
    default void createParserTo() throws Exception {
        var target = createTarget();

        var tokenizer = mock(Tokenizer.class);
        assertDoesNotThrow(() -> target.createParser(tokenizer));
    }

    @Test
    @DisplayName("createParser(re:Reader) [Null parameter]")
    default void createParserReInCaseOfNullParameter(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> target.createParser((Reader) null));
        testReporter.publishEntry(ex.getMessage());
    }

    @Test
    @DisplayName("createParser(re:Reader)")
    default void createParserRe() throws Exception {
        var target = createTarget();

        var reader = Reader.nullReader();
        assertDoesNotThrow(() -> target.createParser(reader));
    }

}
