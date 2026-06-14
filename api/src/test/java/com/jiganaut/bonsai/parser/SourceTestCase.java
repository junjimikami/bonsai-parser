package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockGrammar;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Collector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.TestCase;

/**
 *
 * @author Junji Mikami
 */
interface SourceTestCase<T, R> extends TestCase {

    Source<T> createTarget();

    Collector<? super T, ?, R> createCollector();

    @Test
    @DisplayName("addLayer(gr:Grammar, co:Collector) [gr == null]")
    default void addLayerGrCoWhenGrIsNull(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> target.addLayer(null, createCollector()));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("addLayer(gr:Grammar, co:Collector) [co == null]")
    default void addLayerGrCoWhenCoIsNull(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> target.addLayer(mockGrammar(), null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("addLayer(gr:Grammar, co:Collector)")
    default void addLayerGrCo() throws Exception {
        var target = createTarget();
        var layered = target.addLayer(mockGrammar(), createCollector());

        assertNotEquals(target, layered);
    }

    @Test
    @DisplayName("toTokenizer()")
    default void toTokenizer() throws Exception {
        var target = createTarget();
        var tokenizer = target.toTokenizer();

        assertNotNull(tokenizer);
    }

    @Test
    @DisplayName("toParser(gr:Grammar) [gr == null]")
    default void toParserWhenGrIsNull(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> target.toParser(null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("toParser(gr:Grammar)")
    default void toParser() throws Exception {
        var target = createTarget();
        var parser = target.toParser(mockGrammar());

        assertNotNull(parser);
    }

}