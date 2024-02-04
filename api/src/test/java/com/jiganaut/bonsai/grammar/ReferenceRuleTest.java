package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

class ReferenceRuleTest implements RuleTest {

    @Nested
    class BuilderTest implements RuleTest.BulderTest, QuantifiableTest, ReferenceRelatedTest {

        @Override
        public ReferenceRule.Builder builder() {
            return ReferenceRule.builder("");
        }

        @Nested
        class QuantifierBuilderTest implements QuantifierRuleTest.BuilderTest, ReferenceRelatedTest {
            @Override
            public QuantifierRule.Builder builder() {
                return ReferenceRuleTest.BuilderTest.this.builder().opt();
            }
        }

        @Nested
        class QuantifierTest implements QuantifierRuleTest {
            @Override
            public Quantifiable builder() {
                return ReferenceRuleTest.BuilderTest.this.builder();
            }
        }
    }

    @Override
    public Rule build() {
        return ReferenceRule.builder("").build(Stubs.DUMMY_PRODUCTION_SET);
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public RuleVisitor<Object[], String> visitor() {
        return new TestRuleVisitor<Object[], String>() {
            @Override
            public Object[] visitReference(ReferenceRule reference, String p) {
                return new Object[] { reference, p };
            }
        };
    }

    @Test
    @DisplayName("builder(String) [Null parameter]")
    void builderInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> ReferenceRule.builder(null))
                .printStackTrace();
    }

    @Test
    @DisplayName("builder(String)")
    void builder() throws Exception {
        assertNotNull(ReferenceRule.builder(""));
    }

    @Test
    @DisplayName("builder(String)")
    void get() throws Exception {
        var symbol = "S";
        var reference = ReferenceRule.builder(symbol).build(Stubs.DUMMY_PRODUCTION_SET);
        var production = reference.getProduction();

        assertEquals(symbol, production.getSymbol());
    }

}
