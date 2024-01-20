package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.grammar.Expression.Kind;

class ReferenceExpressionTest implements ExpressionTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Nested
    class BuilderTest implements ExpressionTest.BulderTest, QuantifiableTest, ReferenceRelatedTest {

        @Override
        public ReferenceExpression.Builder builder() {
            return ReferenceExpression.builder("");
        }

    }

    @Override
    public Expression build() {
        return ReferenceExpression.builder("").build(Stubs.DUMMY_PRODUCTION_SET);
    }

    @Test
    @DisplayName("builder(String) [Null parameter]")
    void builderNull() throws Exception {
        assertThrows(NullPointerException.class, () -> ReferenceExpression.builder(null))
                .printStackTrace();
    }

    @Test
    @DisplayName("getKind()")
    void getKind() throws Exception {
        var reference = build();

        assertEquals(Kind.REFERENCE, reference.getKind());
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor)")
    void acceptEv() throws Exception {
        var reference = build();

        reference.accept(new TestExpressionVisitor<Void, Object>() {
            @Override
            public Void visitReference(ReferenceExpression expression, Object p) {
                assertEquals(reference, expression);
                assertNull(p);
                return null;
            }
        });
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor, p:P)")
    void acceptEvP() throws Exception {
        var reference = build();
        var arg = new Object();

        reference.accept(new TestExpressionVisitor<Void, Object>() {
            @Override
            public Void visitReference(ReferenceExpression expression, Object p) {
                assertEquals(reference, expression);
                assertEquals(arg, p);
                return null;
            }
        }, arg);
    }

    @Test
    @DisplayName("builder(String)")
    void get() throws Exception {
        var symbol = "S";
        var reference = ReferenceExpression.builder(symbol).build(Stubs.DUMMY_PRODUCTION_SET);
        var production = reference.get();

        assertEquals(symbol, production.getSymbol());
//        assertEquals(Stubs.DUMMY_EXPRESSION, production.getExpression());
    }

}
