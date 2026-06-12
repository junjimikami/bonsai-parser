package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.MockFactory.mockRule;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author Junji Mikami
 */
class RulesTest {

    @Test
    @DisplayName("pattern(st:String) [st == null]")
    void patternStWhenStIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> Rules.pattern((String) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("pattern(pa:Pattern) [pa == null]")
    void patternPaWhenPaIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> Rules.pattern((Pattern) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("concat(rules:Rule...) [rules == null]")
    void concatRulesWhenRulesIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> Rules.concat((Rule<String>[]) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("oneOf(choices:Rule...) [choices == null]")
    void oneOfChoicesWhenChoicesIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> Rules.oneOf((Rule<String>[]) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("firstOf(choices:Rule...) [choices == null]")
    void firstOfChoicesWhenChoicesIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> Rules.firstOf((Rule<String>[]) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("reference(st:String) [st == null]")
    void referenceWhenStIsNull(TestReporter testReporter) throws Exception {
        var ex = assertThrows(NullPointerException.class, () -> Rules.reference(null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    static Stream<Arguments> nameValueParameters() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, "value"),
                Arguments.of("name", null),
                Arguments.of("", "++++"),
                Arguments.of("****", "")
        );
    }

    @ParameterizedTest
    @MethodSource("nameValueParameters")
    @DisplayName("token(name:String, value:String)")
    void tokenNameValue(String name, String value) throws Exception {
        var actual = Rules.token(name, value);

        assertTrue(actual.test(name, value));
        assertFalse(actual.test("other name", "other value"));
    }

    @ParameterizedTest
    @MethodSource("nameValueParameters")
    @DisplayName("token(name:String)")
    void tokenName(String name, String value) throws Exception {
        var actual = Rules.token(name);

        assertTrue(actual.test(name, value));
        assertFalse(actual.test("other name", "other value"));
    }

    @ParameterizedTest
    @MethodSource("nameValueParameters")
    @DisplayName("matching(value:String)")
    void matching(String name, String value) throws Exception {
        var actual = Rules.matching(value);

        assertTrue(actual.test(name, value));
        assertFalse(actual.test("other name", "other value"));
    }

    static Stream<Arguments> regexParameters() {
        return Stream.of(
                Arguments.of("1", "1"),
                Arguments.of("a", "a"),
                Arguments.of("A", "A"),
                Arguments.of(".", "Z")
        );
    }

    @ParameterizedTest
    @MethodSource("regexParameters")
    @DisplayName("pattern(st:String)")
    void patternSt(String regex, String value) throws Exception {
        var actual = Rules.pattern(regex);

        assertTrue(actual.test(null, value));
    }

    @ParameterizedTest
    @MethodSource("regexParameters")
    @DisplayName("pattern(pa:Pattern)")
    void patternPa(String regex, String value) throws Exception {
        var actual = Rules.pattern(Pattern.compile(regex));

        assertTrue(actual.test(null, value));
    }

    static Stream<List<Rule<String>>> ruleParameters() {
        return Stream.of(
                List.of(mockRule()),
                List.of(mockRule(), mockRule()));
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @EmptySource
    @MethodSource("ruleParameters")
    @DisplayName("concat(Rule...)")
    void concat(List<Rule<String>> list) throws Exception {
        var builder = SequenceRule.<String>builder();
        list.forEach(builder::add);
        var expected = builder.build();
        var actual = Rules.concat(list.toArray(Rule[]::new));

        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @EmptySource
    @MethodSource("ruleParameters")
    @DisplayName("oneOf(Rule...)")
    void oneOf(List<Rule<String>> list) throws Exception {
        var builder = ChoiceRule.<String>builder();
        list.forEach(builder::add);
        var expected = builder.build();
        var actual = Rules.oneOf(list.toArray(Rule[]::new));

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("ruleParameters")
    @DisplayName("firstOf(Rule...)")
    @SuppressWarnings("unchecked")
    void firstOf(List<Rule<String>> list) throws Exception {
        var builder = ChoiceRule.<String>builder();
        list.forEach(builder::add);
        var expected = builder.asShortCircuit().build();
        var actual = Rules.firstOf(list.toArray(Rule[]::new));

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"1", "a", "A", ".", "["})
    @DisplayName("reference(String)")
    void reference(String s) throws Exception {
        var expected = ReferenceRule.of(s);
        var actual = Rules.reference(s);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("empty()")
    void empty() throws Exception {
        var expected = EmptyRule.empty();
        var actual = Rules.empty();

        assertEquals(expected, actual);
        assertEquals(Rule.Kind.EMPTY, actual.getKind());
    }

}
