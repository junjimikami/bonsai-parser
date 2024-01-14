package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChoiceExpressionTest {

    @BeforeEach
    void setUp() throws Exception {
        System.out.println("----");
    }

    @Nested
    class MonkeyTest {
        @Test
        void test() throws Exception {
            var builder = ChoiceExpression.builder();
            assertNotNull(builder);
            assertThrows(IllegalStateException.class, () -> builder.build())
                    .printStackTrace();
            ;
        }

        @Test
        void test2() throws Exception {
            var builder = ChoiceExpression.builder();
            assertThrows(IllegalStateException.class, () -> builder.build(null))
                    .printStackTrace();
        }

        @Test
        void test3() throws Exception {
            var builder = ChoiceExpression.builder();
            assertThrows(NullPointerException.class, () -> builder.add((Expression.Builder) null))
                    .printStackTrace();
            assertThrows(NullPointerException.class, () -> builder.add((String) null))
                    .printStackTrace();
        }

        @Test
        void test4() throws Exception {
            var builder = ChoiceExpression.builder();
            assertEquals(builder, builder.add(set -> Expression.EMPTY));
            assertEquals(builder, builder.add(""));
            assertEquals(builder, builder.addEmpty());
            assertThrows(NullPointerException.class, () -> builder.build())
                    .printStackTrace();
        }

    }
}
