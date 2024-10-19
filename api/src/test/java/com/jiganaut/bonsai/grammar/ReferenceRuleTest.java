package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

class ReferenceRuleTest implements RuleTest, QuantifiableTest {

    @Override
    public ReferenceRule createTarget() {
        return ReferenceRule.of("");
    }

    @Override
    public Kind expectedKind() {
        return Kind.REFERENCE;
    }

    @Override
    public RuleVisitor<Object[], String> createVisitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitReference(ReferenceRule reference, String p) {
                return new Object[] { reference, p };
            }
        };
    }

    @Test
    @DisplayName("of(String) [Null parameter]")
    void ofInCaseOfNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> ReferenceRule.of(null));
    }

    @Test
    @DisplayName("of(String)")
    void of() throws Exception {
        assertNotNull(ReferenceRule.of(""));
    }

    @Test
    @DisplayName("getSymbol")
    void getSymbol() throws Exception {
        var symbol = "S";
        var reference = ReferenceRule.of(symbol);
        var production = reference.lookup(Stubs.DUMMY_PRODUCTION_SET);

        assertEquals(symbol, production.getSymbol());
    }

}
