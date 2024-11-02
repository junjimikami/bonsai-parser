package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;

class RulesTest {

    @Test
    @DisplayName("pattern(st:String) [Null parameter]")
    void patternStInCaseOfNullParameter() throws Exception {

        assertThrows(NullPointerException.class, () -> Rules.pattern((String) null));
    }

    @Test
    @DisplayName("pattern(pa:Pattern) [Null parameter]")
    void patternPaInCaseOfNullParameter() throws Exception {

        assertThrows(NullPointerException.class, () -> Rules.pattern((Pattern) null));
    }

    @Test
    @DisplayName("concat(Rule...) [Null parameter]")
    void concatInCaseOfNullParameter() throws Exception {

        assertThrows(NullPointerException.class, () -> Rules.concat((Rule[]) null));
    }

    @Test
    @DisplayName("oneOf(Rule...) [Null parameter]")
    void oneOfInCaseOfNullParameter() throws Exception {

        assertThrows(NullPointerException.class, () -> Rules.oneOf((Rule[]) null));
    }

    @Test
    @DisplayName("reference(String) [Null parameter]")
    void referenceInCaseOfNullParameter() throws Exception {

        assertThrows(NullPointerException.class, () -> Rules.reference(null));
    }

    @Test
    @DisplayName("quote(String) [Null parameter]")
    void quoteInCaseOfNullParameter() throws Exception {

        assertThrows(NullPointerException.class, () -> Rules.quote(null));
    }

    static Stream<String> regexParameters() {
        return Stream.of("1", "a", "A", ".");
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("regexParameters")
    @DisplayName("pattern(st:String)")
    void patternSt(String regex) throws Exception {
        var expected = PatternRule.of(regex);
        var actual = Rules.pattern(regex);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("regexParameters")
    @DisplayName("pattern(pa:Pattern)")
    void patternPa(String regex) throws Exception {
        var expected = PatternRule.of(regex);
        var actual = Rules.pattern(Pattern.compile(regex));

        assertEquals(expected, actual);
    }

    static Stream<List<Rule>> ruleParameters() {
        return Stream.of(
                List.of(mock(Rule.class)),
                List.of(mock(Rule.class), mock(Rule.class)));
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("ruleParameters")
    @DisplayName("concat(Rule...)")
    void concat(List<Rule> list) throws Exception {
        var builder = SequenceRule.builder();
        list.forEach(builder::add);
        var expected = builder.build();
        var actual = Rules.concat(list.toArray(Rule[]::new));

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("ruleParameters")
    @DisplayName("oneOf(Rule...)")
    void oneOf(List<Rule> list) throws Exception {
        var builder = ChoiceRule.builder();
        list.forEach(builder::add);
        var expected = builder.build();
        var actual = Rules.oneOf(list.toArray(Rule[]::new));

        assertEquals(expected, actual);
    }

    static Stream<String> stringParameters() {
        return Stream.concat(regexParameters(), Stream.of("["));
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("stringParameters")
    @DisplayName("reference(String)")
    void reference(String s) throws Exception {
        var expected = ReferenceRule.of(s);
        var actual = Rules.reference(s);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EmptySource
    @MethodSource("stringParameters")
    @DisplayName("quote(String)")
    void quote(String s) throws Exception {
        var expected = PatternRule.of(Pattern.quote(s));
        var actual = Rules.quote(s);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("empty()")
    void empty() throws Exception {
        var expected = Rule.EMPTY;
        var actual = Rules.empty();

        assertEquals(expected, actual);
        assertEquals(Rule.Kind.EMPTY, actual.getKind());
    }

}
