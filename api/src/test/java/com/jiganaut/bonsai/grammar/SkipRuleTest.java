package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

class SkipRuleTest implements RuleTest {

    @Override
    public SkipRule createTarget() {
        return expectedRule().skip();
    }

    @Override
    public Kind expectedKind() {
        return Kind.SKIP;
    }

    @Override
    public RuleVisitor<Object[], String> createVisitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitSkip(SkipRule skip, String p) {
                return new Object[] { skip, p };
            }
        };
    }

    Skippable expectedRule() {
        return PatternRule.of("0");
    }

    @Test
    @DisplayName("getRule()")
    void getRule() throws Exception {
        var rule = createTarget();

        var expected = (PatternRule) expectedRule();
        var actual = (PatternRule) rule.getRule();
        assertEquals(expected.getKind(), actual.getKind());
        assertEquals(expected.getPattern().pattern(), actual.getPattern().pattern());
    }

}
