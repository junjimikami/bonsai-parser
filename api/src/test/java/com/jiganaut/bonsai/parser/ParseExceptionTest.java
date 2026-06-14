package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockErrorNode;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

/**
 *
 * @author Junji Mikami
 */
class ParseExceptionTest {

    @Test
    @DisplayName("ParseException(en:ErrorNode) [en == null]")
    void constructorWhenEnIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> new ParseException(null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("ParseException(en:ErrorNode)")
    void constructor() throws Exception {
        var errorNode = mockErrorNode();
        var ex = new ParseException(errorNode);

        assertSame(errorNode, ex.getErrorNode());
    }

}