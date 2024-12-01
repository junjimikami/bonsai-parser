package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.jiganaut.bonsai.TestCase;

interface TokenizerTestCase extends TestCase {

    @Override
    Tokenizer createTarget();

    Iterator<Token> expectedIterator();

    Iterator<Long> expectedLineNumbers();

    Iterator<Long> expectedIndexes();

    default Token createToken(String name, String value) {
        return new Token() {

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

    @Test
    @DisplayName("hasNext()")
    default void hasNext() throws Exception {
        var target = createTarget();

        assertEquals(expectedIterator().hasNext(), target.hasNext());
    }

    @Test
    @DisplayName("hasNextName(String)")
    default void hasNextName() throws Exception {
        var target = createTarget();

        var expected = expectedIterator();
        if (expected.hasNext()) {
            var next = expected.next();
            assertTrue(target.hasNextName(next.getName()));
        } else {
            assertFalse(target.hasNextName(null));
        }
    }

    @Test
    @DisplayName("hasNextValue(st:String) [Null parameter]")
    default void hasNextValueStInCaseOfNullParameter() throws Exception {
        var target = createTarget();

        assertThrows(NullPointerException.class, () -> target.hasNextValue((String) null));
    }

    @Test
    @DisplayName("hasNextValue(st:String)")
    default void hasNextValueSt() throws Exception {
        var target = createTarget();

        var expected = expectedIterator();
        if (expected.hasNext()) {
            var next = expected.next();
            assertTrue(target.hasNextValue(next.getValue()));
        } else {
            assertFalse(target.hasNextValue(""));
        }
    }

    @Test
    @DisplayName("hasNextValue(pa:Pattern) [Null parameter]")
    default void hasNextValuePaInCaseOfNullParameter() throws Exception {
        var target = createTarget();

        assertThrows(NullPointerException.class, () -> target.hasNextValue((Pattern) null));
    }

    @Test
    @DisplayName("hasNextValue(pa:Pattern)")
    default void hasNextValuePa() throws Exception {
        var target = createTarget();

        var expected = expectedIterator();
        if (expected.hasNext()) {
            var next = expected.next();
            var pattern = Pattern.compile(next.getValue());
            assertTrue(target.hasNextValue(pattern));
        } else {
            var pattern = Pattern.compile("");
            assertFalse(target.hasNextValue(pattern));
        }
    }

    @Test
    @DisplayName("next()")
    default void next() throws Exception {
        var target = createTarget();

        var expected = expectedIterator();
        while (expected.hasNext()) {
            var next = expected.next();
            var actual = target.next();
            assertEquals(next.getName(), actual.getName());
            assertEquals(next.getValue(), actual.getValue());
        }
        assertFalse(target.hasNext());
        assertThrows(NoSuchElementException.class, () -> target.next());
    }

    @Test
    @DisplayName("nextName()")
    default void nextName() throws Exception {
        var target = createTarget();

        var expected = expectedIterator();
        while (expected.hasNext()) {
            var next = expected.next();
            assertEquals(next.getName(), target.nextName());
        }
        assertFalse(target.hasNext());
        assertThrows(NoSuchElementException.class, () -> target.nextName());
    }

    @Test
    @DisplayName("nextValue()")
    default void nextValue() throws Exception {
        var target = createTarget();

        var expected = expectedIterator();
        while (expected.hasNext()) {
            var next = expected.next();
            assertEquals(next.getValue(), target.nextValue());
        }
        assertFalse(target.hasNext());
        assertThrows(NoSuchElementException.class, () -> target.nextValue());
    }

    @Test
    @DisplayName("getName()")
    default void getName() throws Exception {
        var target = createTarget();

        var expected = expectedIterator();
        while (expected.hasNext()) {
            var next = expected.next();
            target.next();
            assertEquals(next.getName(), target.getName());
        }
        assertFalse(target.hasNext());
    }

    @Test
    @DisplayName("getValue()")
    default void getValue() throws Exception {
        var target = createTarget();

        var expected = expectedIterator();
        while (expected.hasNext()) {
            var next = expected.next();
            target.next();
            assertEquals(next.getValue(), target.getValue());
        }
        assertFalse(target.hasNext());
    }

    @Test
    @DisplayName("getLineNumber()")
    default void getLineNumber() throws Exception {
        var target = createTarget();

        assertEquals(1, target.getLineNumber());
        var expected = expectedLineNumbers();
        while (expected.hasNext()) {
            var next = expected.next();
            target.next();
            assertEquals(next, target.getLineNumber());
        }
    }

    @Test
    @DisplayName("getIndex()")
    default void getIndex() throws Exception {
        var target = createTarget();

        assertEquals(0, target.getIndex());
        var expected = expectedIndexes();
        while (expected.hasNext()) {
            var next = expected.next();
            target.next();
            assertEquals(next, target.getIndex());
        }
    }

    @ParameterizedTest
    @MethodSource("tokenizerOperations")
    @DisplayName("Tokenizer operation [Closed input stream]")
    default void tokenizerOperationInCaseOfClosedInputStream(Consumer<Tokenizer> operation) throws Exception {
        var target = createTarget();
        target.close();

        assertThrows(IllegalStateException.class, () -> operation.accept(target));
    }

    static Stream<Consumer<Tokenizer>> tokenizerOperations() {
        return Stream.of(
                target -> target.hasNext(),
                target -> target.hasNextName(""),
                target -> target.hasNextValue(""),
                target -> target.hasNextValue(Pattern.compile("")),
                target -> target.next(),
                target -> target.nextName(),
                target -> target.nextValue());
    }

    @Test
    @DisplayName("close()")
    default void close() throws Exception {
        var target = createTarget();

        assertDoesNotThrow(() -> target.close());
    }

}
