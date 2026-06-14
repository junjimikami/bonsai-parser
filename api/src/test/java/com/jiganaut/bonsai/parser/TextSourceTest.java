package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockGrammar;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

/**
 *
 * @author Junji Mikami
 */
class TextSourceTest implements SourceTestCase<String, String> {

    @Override
    public Source<String> createTarget() {
        return TextSource.of("ab");
    }

    @Override
    public Collector<? super String, ?, String> createCollector() {
        return Collectors.joining();
    }

    @Test
    @DisplayName("of(re:Reader) [re == null]")
    void ofReWhenReIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> TextSource.of((java.io.Reader) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("of(te:CharSequence) [te == null]")
    void ofTeWhenTeIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> TextSource.of((CharSequence) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("of(re:Reader) [re is empty]")
    void ofReWhenReIsEmpty() throws Exception {
        var source = TextSource.of(Reader.nullReader());

        assertNotNull(source);
        assertFalse(source.toTokenizer().hasNext());
    }

    @Test
    @DisplayName("of(te:CharSequence) [te is empty]")
    void ofTeWhenTeIsEmpty() throws Exception {
        var source = TextSource.of("");

        assertNotNull(source);
        assertFalse(source.toTokenizer().hasNext());
    }

    @Test
    @DisplayName("of(re:Reader)")
    void ofRe() throws Exception {
        var source = TextSource.of(new StringReader("ab"));

        assertNotNull(source);
        assertTrue(source.toTokenizer().hasNext());
    }

    @Test
    @DisplayName("of(te:CharSequence)")
    void ofTe() throws Exception {
        var source = TextSource.of("ab");

        assertNotNull(source);
        assertTrue(source.toTokenizer().hasNext());
    }

    @Test
    @DisplayName("addLayer(gr:Grammar) [gr == null]")
    void addLayerGrWhenGrIsNull(TestReporter testReporter) throws Exception {
        var source = TextSource.of("ab");

        var ex = assertThrows(NullPointerException.class, () -> source.addLayer(null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("addLayer(gr:Grammar)")
    void addLayerGr() throws Exception {
        var source = TextSource.of("ab");
        var layered = source.addLayer(mockGrammar());

        assertNotEquals(source, layered);
    }

}