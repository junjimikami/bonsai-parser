package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

/**
 *
 * @author Junji Mikami
 */
class BinarySourceTest implements SourceTestCase<Byte, List<Byte>> {

    @Override
    public Source<Byte> createTarget() {
        return BinarySource.of(new byte[] { 1, 2 });
    }

    @Override
    public Collector<? super Byte, ?, List<Byte>> createCollector() {
        return Collectors.toList();
    }

    @Test
    @DisplayName("of(stream:InputStream) [stream == null]")
    void ofStreamWhenStreamIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> BinarySource.of((InputStream) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("of(bytes:byte[]) [bytes == null]")
    void ofBytesWhenBytesIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> BinarySource.of((byte[]) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("of(stream:InputStream) [stream is empty]")
    void ofStreamWhenStreamIsEmpty() throws Exception {
        var source = BinarySource.of(new ByteArrayInputStream(new byte[0]));

        assertNotNull(source);
        assertFalse(source.toTokenizer().hasNext());
    }

    @Test
    @DisplayName("of(bytes:byte[]) [bytes is empty]")
    void ofBytesWhenBytesIsEmpty() throws Exception {
        var source = BinarySource.of(new byte[0]);

        assertNotNull(source);
        assertFalse(source.toTokenizer().hasNext());
    }

    @Test
    @DisplayName("of(stream:InputStream)")
    void ofStream() throws Exception {
        var source = BinarySource.of(new ByteArrayInputStream(new byte[] { 1, 2 }));

        assertNotNull(source);
        assertTrue(source.toTokenizer().hasNext());
    }

    @Test
    @DisplayName("of(bytes:byte[])")
    void ofBytes() throws Exception {
        var source = BinarySource.of(new byte[] { 1, 2 });

        assertNotNull(source);
        assertTrue(source.toTokenizer().hasNext());
    }

}