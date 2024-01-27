package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.grammar.Expression.Kind;

class ReferenceExpressionTest implements ExpressionTest {

    @Nested
    class BuilderTest implements ExpressionTest.BulderTest, QuantifiableTest, ReferenceRelatedTest {

        @Override
        public ReferenceExpression.Builder builder() {
            return ReferenceExpression.builder("");
        }

        @Nested
        class QuantifierBuilderTest implements QuantifierExpressionTest.BuilderTest, ReferenceRelatedTest {
            @Override
            public QuantifierExpression.Builder builder() {
                return ReferenceExpressionTest.BuilderTest.this.builder().opt();
            }
        }

        @Nested
        class QuantifierTest implements QuantifierExpressionTest {
            @Override
            public Quantifiable builder() {
                return ReferenceExpressionTest.BuilderTest.this.builder();
            }
        }
    }

    @Override
    public Expression build() {
        return ReferenceExpression.builder("").build(Stubs.DUMMY_PRODUCTION_SET);
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public ExpressionVisitor<Object[], String> elementVisitor() {
        return new TestExpressionVisitor<Object[], String>() {
            @Override
            public Object[] visitReference(ReferenceExpression reference, String p) {
                return new Object[] { reference, p };
            }
        };
    }

    @Test
    @DisplayName("builder(String) [Null parameter]")
    void builderInCaseNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> ReferenceExpression.builder(null))
                .printStackTrace();
    }

    @Test
    @DisplayName("builder(String)")
    void builder() throws Exception {
        assertNotNull(ReferenceExpression.builder(""));
    }

    @Test
    @DisplayName("builder(String)")
    void get() throws Exception {
        var symbol = "S";
        var reference = ReferenceExpression.builder(symbol).build(Stubs.DUMMY_PRODUCTION_SET);
        var production = reference.getProduction();

        assertEquals(symbol, production.getSymbol());
    }

}
